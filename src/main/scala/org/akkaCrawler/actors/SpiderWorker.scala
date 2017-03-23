package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.event.{Logging, LoggingReceive}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderDriver}
import org.akkaCrawler.messages.Messages._
import org.akkaCrawler.spiders.{Spider, SpiderFactory}
import org.joda.time.LocalDateTime

import scala.collection.mutable
import scala.concurrent.ExecutionContext

/**
  * Runs a given Crawler Spider or a part of
  * given spider work
  */
class SpiderWorker[T <: Spider] extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher

  var driver: SpiderDriver[T] = _
  /** Work state */
  var lastTaskResult: Option[Result] = None
  var runningTask: Option[Task] = None
  var taskStartTime: Option[LocalDateTime] = None

  override def preStart(): Unit = {

  }

  def receive: Receive = {
    case c: CommandRequest  =>
      toRunning(c)

    case QueryLastTaskResult =>
      // TODO: security vunerability
      log.info(s"Replying to ${sender().path.toStringWithoutAddress} " +
        s"last task result: [${lastTaskResult}]")
      sender() ! LastTaskResult(result = lastTaskResult)

  }

  def working: Receive = {
    case c: CommandRequest => {
      // TODO: security vunerability
      val response = Busy(
        task = runningTask.getOrElse(
          CoordinationWorkCorruption(s"Whoops seems like I (${self}) am confused!")),
        startTime =  taskStartTime.getOrElse(new LocalDateTime()))
      log.info(s"Replying to ${sender().path.toStringWithoutAddress} " +
        s"current status: [${response}]")
      sender() ! response
    }


    case QueryLastTaskResult =>
      // TODO: security vunerability
      log.info(s"Replying to ${sender().path.toStringWithoutAddress} " +
        s"last task result: [${lastTaskResult}]")
      sender() ! LastTaskResult(result = lastTaskResult)

    case _ =>
  }

  private def toRunning(c: CommandRequest) = {
    c match {
      case StartCrawl(task) =>
        runJob(task.url, task.spiderName)
        context.become(working)
      case _ => // do nothing
    }
  }

  private def registerJob(task: Task): Unit = {

  }

  private def runJob(url: String, spiderName: String) = {
    val spiderObj = SpiderFactory(spiderName).spiderFor
    val spider = spiderObj()


    driver = SpiderFactory(spiderName).driverFor(url, spiderName)
  }
}

object SpiderWorker {
  def props(spiderName: String): Props = {
    Props(
      classOf[SpiderWorker[_]],
      spiderName
    )
  }

  def run(spiderName: String)(implicit system: ActorSystem): ActorRef = {
    system.actorOf(props(spiderName))
  }
}
