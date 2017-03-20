package org.akkaCrawler.drivers

import java.time.LocalDateTime

import org.akkaCrawler.spiders.BerlinStartupJobs

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Driver to run crawling of BerlinStartupJobs
  */
class BerlinStartupJobsDriver extends SpiderDriverOnBlockingApi[BerlinStartupJobs] {

  def spiderName = "BerlinStartupJobsSpider"

  // TODO
  override def killRun(run: SpiderDriverRunHandle[BerlinStartupJobs]) = {}

  def run(s: BerlinStartupJobs) = {
    new SpiderDriverRunHandle[BerlinStartupJobs](
      driver = this,
      started = new LocalDateTime(),
      spider = s,
      Future {
        s.run()
      })
  }
}

object BerlinStartupJobsDriver extends SpiderDriverCompanionObject[BerlinStartupJobs] {
  def apply: BerlinStartupJobsDriver = new BerlinStartupJobsDriver()
}
