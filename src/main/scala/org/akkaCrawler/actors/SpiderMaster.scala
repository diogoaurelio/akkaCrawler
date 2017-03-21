package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, Props}
import org.akkaCrawler.drivers.RetryableDriverException
import org.akkaCrawler.spiders.SpiderFactory

import scala.concurrent.ExecutionContext

/**
  * Master Actor coordenating several spiders for one job
  */
class SpiderMaster extends Actor with ActorLogging{

  implicit val ec: ExecutionContext = context.dispatcher

  def receive: Receive = {
    case _ =>
  }

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


}
