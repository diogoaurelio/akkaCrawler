package org.akkaCrawler.spiders

import net.ruippeixotog.scalascraper.browser.{HtmlUnitBrowser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.akkaCrawler.drivers.SpiderDriver

import scala.concurrent.Future


/**
  * Generic contract for spiders
  */
trait Spider {

  def run(url: String): Future[CrawlingRun]

}

trait SpiderCompanionObject[T <: Spider] {
  def rootUrl: String
  def _mainUrls: List[String]
  def mainUrls: List[String] = _mainUrls.map(s => rootUrl + "/" + s)
  def maxDeepth: Int
  def getDriver(url: String, spiderName: String): SpiderDriver[T]
}


sealed class CrawlingRun

// TODO: define how to extract crawling contents
case class CrawlingSuccessful() extends CrawlingRun

case class CrawlingFailed()
