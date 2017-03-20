package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging}
import akka.event.{Logging, LoggingReceive}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderDriver}
import org.akkaCrawler.spiders.{Spider, SpiderFactory}

/**
  * Runs a given Crawler Spider or a part of
  * given spider work
  */
class SpiderWorker[T <: Spider](val spiderName: String, driverClassName: String) extends Actor with ActorLogging {

  var driver: SpiderDriver[T] = _

  override def preStart(): Unit = {
    try {
      driver = SpiderFactory[T] (spiderName).driverFor(driverClassName)
    } catch {
      case t: Throwable => throw RetryableDriverException(s"SpiderWorker driver actor could not initialize " +
        s"driver ${driverClassName} for spider ${spiderName} because driver constructor throws exception. " +
        s"Restarting spider-worker driver actor...", t)
    }
  }

  def receive: Receive = {
    case _ =>
  }

}
