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
      case t: Throwable => throw RetryableDriverException("SpiderWorker Driver actor could not initialize driver " +
        "because driver constructor throws exception. Restarting spider-worker driver actor...", t)
    }
  }

  def receive: Receive = {
    case _ =>
  }

}
