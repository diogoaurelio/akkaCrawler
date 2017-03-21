package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.{Logging, LoggingReceive}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderDriver}
import org.akkaCrawler.messages.Messages
import org.akkaCrawler.spiders.{Spider, SpiderFactory}

/**
  * Runs a given Crawler Spider or a part of
  * given spider work
  */
class SpiderWorker[T <: Spider] extends Actor with ActorLogging {

  var driver: SpiderDriver[T] = _

  override def preStart(): Unit = {

  }

  def receive: Receive = {
    case Messages.Run(url, spiderName) =>
  }

  def working: Receive = {
    case _ =>
  }

  def toWork = become(working)

  def runJob(url: String, spiderName: String) = {
    var spiderObj = SpiderFactory(spiderName).spiderFor
    driver = spiderObj.getDriver(url, spiderName)
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
