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

import scala.concurrent.Future
import org.slf4j.LoggerFactory

/**
  * Example crawler
  * TODO
  */
class BerlinStartupJobs extends Spider {

  val log = LoggerFactory.getLogger(classOf[BerlinStartupJobs])

  lazy val browser = jsoupBrowser()
  lazy val rootUrl = "http://berlinstartupjobs.com"
  lazy val maxDeepth = 2

  def jsoupBrowser = () => JsoupBrowser()
  def htmlBrowser = () => HtmlUnitBrowser()
  def doc: Document = browser.get(rootUrl)

  def run(): Unit = {
    println("=== Berlin Startup Jobs Header ===")
    //println(doc.head)

    println("=== Berlin Startup Jobs Body ===")
    doc >> elementList("h2 .product-listing-h2") |> println

    doc >> extractor("a") |> println
    //println(doc.body)
    doc >> extractor("h2.product-listing-h2") |> println

    println("=== Berlin Startup Jobs XXXX ===")
    val items = doc >> elementList(".intro-text")
    items.foreach(elem => println(elem >?> element("href")))

    println("==================")
  }
}