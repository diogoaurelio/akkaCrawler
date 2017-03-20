package org.akkaCrawler

/**
  * General settings
  */
object AppSettings {

  lazy val rootUrl = "http://berlinstartupjobs.com"

  lazy val berlinStartupJobsMainUrls: Seq[String] = Seq(rootUrl+"/engineering")

}
