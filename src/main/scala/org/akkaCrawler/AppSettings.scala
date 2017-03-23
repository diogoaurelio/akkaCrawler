package org.akkaCrawler

import scala.concurrent.duration._

/**
  * General settings
  */
object AppSettings {

  lazy val rootUrl = "http://berlinstartupjobs.com"


  /**
    * Used for Acknowledge messages for work scheduling between
    * SpiderMaster and SpiderWorker
    */
  lazy val defaultPingDuration = 100 milliseconds

  lazy val maxNumberRetries = 3


  def getSpiderClassName(url: String) = url match {
    case "http://berlinstartupjobs.com" => "BerlinStartupJobs"
  }

  lazy val berlinStartupJobsMainUrls: Seq[String] = Seq(rootUrl+"/engineering")

}
