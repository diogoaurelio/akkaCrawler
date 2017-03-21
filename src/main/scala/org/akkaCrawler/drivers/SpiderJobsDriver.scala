package org.akkaCrawler.drivers

import org.joda.time.LocalDateTime
import org.akkaCrawler.spiders.{BerlinStartupJobs, Spider}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Driver to run crawling of BerlinStartupJobs
  */
class SpiderJobsDriver[T <: Spider](val url: String = null, val spiderName: String) extends SpiderDriver[T] {

  def run(s: T) = {
    new SpiderDriverRunHandle[T](
      driver = this,
      started = new LocalDateTime(),
      spider = s,
      stateHandle = s.run(url)
    )
  }
}
