package org.akkaCrawler.drivers

import org.joda.time.LocalDateTime

import org.akkaCrawler.spiders.Spider

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success}

/**
  * Spider driver
  */
trait SpiderDriver[T <: Spider] {

  def killRun(run: SpiderDriverRunHandle[T]): Unit = {}

  def run(s: T): SpiderDriverRunHandle[T]

  /**
    * Get the current driver run state for a given driver run represented by the handle.
    */
  def getDriverRunState(run: SpiderDriverRunHandle[T]): DriverRunState[T] = {
    val runState = run.stateHandle.asInstanceOf[Future[DriverRunState[T]]]

    if (runState.isCompleted)
      runState.value.get match {
        case s: Success[DriverRunState[T]] => s.value
        case f: Failure[DriverRunState[T]] => throw f.exception
      }
    else
      DriverRunOngoing[T](this, run)
  }
}

trait SpiderDriverCompanionObject[T <: Spider] {
  def apply(url: String, spiderName: String): SpiderDriver[T]
}


class SpiderDriverRunHandle[T <: Spider](val driver: SpiderDriver[T], val started: LocalDateTime, val spider: T, var stateHandle: Any)


/**
  * Base class for driver run's state, contains reference to driver instance (e.g. to execute code for termination)
  */
sealed abstract class DriverRunState[T <: Spider](val driver: SpiderDriver[T])

/**
  * Driver run state: spider crawling is still being executed
  */
case class DriverRunOngoing[T <: Spider](override val driver: SpiderDriver[T], val runHandle: SpiderDriverRunHandle[T]) extends DriverRunState[T](driver)

/**
  * Driver run state: spider crawling has finished succesfully. The driver actor embedding the driver having sucessfully
  * executed the crawling will return a success message to the view actor initiating the transformation.
  */
case class DriverRunSucceeded[T <: Spider](override val driver: SpiderDriver[T], comment: String) extends DriverRunState[T](driver)

/**
  * Driver run state: spider crawling has terminated with an error. The driver actor embedding the driver having failed
  * at executing the spider crawling will return a failure message to the view actor initiating the transformation.
  */
case class DriverRunFailed[T <: Spider](override val driver: SpiderDriver[T], reason: String, cause: Throwable) extends DriverRunState[T](driver)

/**
  * Exceptions occurring in a driver that merit a retry without counting against the retry limit. These will be escalated to the driver actor
  * to cause a driver actor restart.
  */
case class RetryableDriverException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)
