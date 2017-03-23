package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import org.akkaCrawler.AppSettings
import org.akkaCrawler.drivers.RetryableDriverException
import org.akkaCrawler.messages.Messages._
import org.akkaCrawler.spiders.SpiderFactory
import org.joda.time.LocalDateTime

import scala.collection.mutable.Queue
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Master Actor coordenating several spiders for one job
  */
class SpiderMaster extends Actor with ActorLogging{

  implicit val ec: ExecutionContext = context.dispatcher

  val pingDuration = AppSettings.defaultPingDuration
  val maxPingRetries = AppSettings.maxNumberRetries

  val jobsQueue = new Queue[String]()
  var runningJobs = mutable.HashMap[String, LocalDateTime]()
  var jobsActorMapping = mutable.HashMap[String, ActorRef]()

  override def preStart(): Unit = {
    super.preStart()

    // Hard-coded temporary for ease of use until akka-http service is built
    self ! StartJob(AppSettings.rootUrl)
  }

  def receive: Receive = {

    case StartJob(url) =>
      log.info(s"Starting new crawling job for URL: ${url}")
      launchSpiders(spiderName = AppSettings.getSpiderClassName(url))

    case AcknowledgeWork(url: String, startTime: LocalDateTime) =>
      updateRunningJobs(url, startTime, sender())

    case AckTick(retries: Int, worker: ActorRef, url: String) =>
      if (jobsActorMapping(url) != worker && retries < maxPingRetries) {
        log.warning("Changing job target for different node, since " +
          s"worker ${worker.path.toStringWithoutAddress} ")

      } else if (jobsActorMapping(url) != worker && retries >= maxPingRetries) {
        log.warning("Changing job target for different node, since " +
          "worker")
      }


    case _ => //do nothing
  }

  /**
    * Load balances work of crawling a website across
    * distinct SpiderWorkers
    *
    * @param spiderName
    * @return
    */
  private def launchSpiders(spiderName: String) = {
    try {
      val spiderObj = SpiderFactory(spiderName).spiderFor
      val urls: List[String] = spiderObj.mainUrls

      context.system.actorOf(SpiderWorker.props(spiderName))

    } catch {
      case t: Throwable => throw RetryableDriverException(s"SpiderWorker driver actor could not initialize " +
        s"driver for spider ${spiderName} because driver constructor throws exception. " +
        s"Restarting spider-worker driver actor...", t)
    }
  }

  private def delegateWork(url: String, spiderName: String, worker: ActorRef): Unit = {
    worker ! StartCrawl(url, spiderName)
  }

  /**
    * Tick used to confirm SpiderWorker accepted Work
    */
  private def tickForAck(worker: ActorRef, retries: Int, url: String): Unit = {
    context.system.scheduler.scheduleOnce(pingDuration, self, AckTick(retries+1, worker, url))
  }

  private def updateRunningJobs(url: String, startTime: LocalDateTime, worker: ActorRef): Unit = {
    runningJobs += (url -> startTime)
    jobsActorMapping += (url -> worker)
  }
}

object SpiderMaster {
  def run()(implicit system: ActorSystem): ActorRef = {
    system.actorOf(Props[SpiderMaster])
  }
}