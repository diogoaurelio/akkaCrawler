package org.akkaCrawler.drivers

import org.joda.time.LocalDateTime
import org.akkaCrawler.spiders.{BerlinStartupJobs, Spider}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Driver to run crawling of BerlinStartupJobs
  */
class BerlinStartupJobsDriver(val url: String = null, val spiderName: String) extends SpiderDriver[BerlinStartupJobs] {

  def run(s: BerlinStartupJobs) = {
    new SpiderDriverRunHandle[BerlinStartupJobs](
      driver = this,
      started = new LocalDateTime(),
      spider = s,
      stateHandle = s.run(url)
    )
  }
}

object BerlinStartupJobsDriver extends SpiderDriverCompanionObject[BerlinStartupJobs]{
  def apply(url: String, spiderName: String) = new BerlinStartupJobsDriver(url, spiderName)
}
