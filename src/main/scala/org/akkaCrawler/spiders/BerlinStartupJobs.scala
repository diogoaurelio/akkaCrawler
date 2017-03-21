package org.akkaCrawler.spiders

import java.io.PrintStream
import java.time.LocalDateTime

import com.typesafe.config.ConfigFactory
import net.ruippeixotog.scalascraper.browser.{HtmlUnitBrowser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.util.ProxyUtils
import net.ruippeixotog.scalascraper.util.Validated._
import org.akkaCrawler.drivers.{SpiderDriver, SpiderJobsDriver}

import scala.concurrent.{ExecutionContext, Future}
import org.slf4j.LoggerFactory

/**
  * Example crawler
  * TODO
  */
class BerlinStartupJobs(implicit ec: ExecutionContext) extends Spider {
  import BerlinStartupJobs._

  val log = LoggerFactory.getLogger(classOf[BerlinStartupJobs])

  lazy val browser = JsoupBrowser()

  def doc: Document = browser.get(rootUrl)

  def run(url: String): Future[CrawlingRun] = Future {
    println("=== Berlin Startup Jobs Header ===")
    println(doc.head)

    println("=== Berlin Startup Jobs Body ===")
    doc >> elementList("h2 .product-listing-h2") |> println

    doc >> extractor("a") |> println
    //println(doc.body)
    doc >> extractor("h2.product-listing-h2") |> println

    println("=== Berlin Startup Jobs XXXX ===")
    val items = doc >> elementList(".intro-text")
    items.foreach(elem => println(elem >?> element("href")))

    println("==================")
    CrawlingSuccessful()
  }
}

object BerlinStartupJobs extends SpiderCompanionObject[BerlinStartupJobs] {

  def rootUrl = "http://berlinstartupjobs.com"
  def _mainUrls: List[String] = List("engineering")
  def maxDeepth = 2
  def getDriver(url: String, spiderName: String) = new SpiderJobsDriver[BerlinStartupJobs](url, spiderName)

}