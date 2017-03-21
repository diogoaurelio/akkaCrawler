package org.akkaCrawler.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.{Logging, LoggingReceive}
import org.akkaCrawler.drivers.{RetryableDriverException, SpiderDriver}
import org.akkaCrawler.messages.Messages._
import org.akkaCrawler.spiders.{Spider, SpiderFactory}

import scala.concurrent.ExecutionContext

/**
  * Runs a given Crawler Spider or a part of
  * given spider work
  */
class SpiderWorker[T <: Spider] extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher

  var driver: SpiderDriver[T] = _

  override def preStart(): Unit = {

  }

  def receive: Receive = {
    case c: CommandRequest  =>
      toRunning(c)

  }

  def working: Receive = {
    case _ =>
  }

  def toRunning(c: CommandRequest) = {
    c match {
      case StartCrawl(url: String, spiderName: String) =>
        runJob(url, spiderName)
        context.become(working)
      case _ => // do nothing
    }

  }

  def runJob(url: String, spiderName: String) = {
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
}
