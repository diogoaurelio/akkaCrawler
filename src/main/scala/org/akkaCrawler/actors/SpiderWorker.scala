package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.{Logging, LoggingReceive}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderDriver}
import org.akkaCrawler.spiders.{Spider, SpiderFactory}

/**
  * Runs a given Crawler Spider or a part of
  * given spider work
  */
class SpiderWorker[T <: Spider](val spiderName: String) extends Actor with ActorLogging {

  var driver: SpiderDriver[T] = _

  val spider = SpiderFactory(spiderName).spiderFor
  driver = spider.getDriver()


  override def preStart(): Unit = {

  }

  def receive: Receive = {
    case _ =>
  }

}

object SpiderWorker {
  def props(spiderName: String): Props = {
    Props(
      classOf[SpiderWorker[_]],
      spiderName

    )
  }
}
