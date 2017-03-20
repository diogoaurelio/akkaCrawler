package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, Props}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderJobsDriver}
import org.akkaCrawler.spiders.SpiderFactory

/**
  * Master Actor coordenating several spiders for one job
  */
class SpiderMaster extends Actor with ActorLogging{


  def receive: Receive = {
    case _ =>
  }

  private def launchSpiders(spiderName: String) = {
    try {
      val spider = SpiderFactory(spiderName).spiderFor
      val driver = spider.getDriver()
      context.system.actorOf(SpiderWorker.props(spiderName))
    } catch {
      case t: Throwable => throw RetryableDriverException(s"SpiderWorker driver actor could not initialize " +
        s"driver for spider ${spiderName} because driver constructor throws exception. " +
        s"Restarting spider-worker driver actor...", t)
    }
  }


}
