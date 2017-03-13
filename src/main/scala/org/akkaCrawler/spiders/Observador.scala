package org.akkaCrawler.spiders

import net.ruippeixotog.scalascraper.browser.{HtmlUnitBrowser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document

/**
  * Example App
  */
object Observador extends App with Spider {

  lazy val browser = jsoupBrowser()
  lazy val rootUrl = "http://observador.pt"
  lazy val maxDeepth = 2

  def jsoupBrowser = () => JsoupBrowser()
  def htmlBrowser = () => HtmlUnitBrowser()
  def doc: Document = browser.get(rootUrl)

  println("=== http://observador.pt Header ===")
  println(doc.head)

  println("=== http://observador.pt Body ===")
  println(doc.body)

  println("=== http://observador.pt YOYOYO ===")
  doc >> extractor(".logo img", attr("src")) |> println

  println("==================")
  println()

  doc >> ".small-news-list h4 > a" foreach println

  println("==================")
  println()

  //doc >> ".small-news-list h4 > a" foreach println

}
