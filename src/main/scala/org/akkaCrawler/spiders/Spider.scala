package org.akkaCrawler.spiders

import net.ruippeixotog.scalascraper.browser.{HtmlUnitBrowser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.akkaCrawler.drivers.DriverRunState
import scala.concurrent.Future


/**
  * Generic contract for spiders
  */
trait Spider {

  val rootUrl: String
  val maxDeepth: Int

  def jsoupBrowser: () => JsoupBrowser
  def htmlBrowser: () => HtmlUnitBrowser
  def doc: Document

  def getTitle = doc >> text("title")

  def mainCategories: List[String]

  def run(): Future[DriverRunState[Spider]]

}
