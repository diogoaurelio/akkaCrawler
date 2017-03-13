package org.akkaCrawler.spiders

import java.io.PrintStream

import com.typesafe.config.ConfigFactory
import net.ruippeixotog.scalascraper.browser.{HtmlUnitBrowser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.util.ProxyUtils
import net.ruippeixotog.scalascraper.util.Validated._

/**
  * Example App
  */
object BerlinStartupJobs extends App with Spider {

  lazy val browser = jsoupBrowser()
  lazy val rootUrl = "http://berlinstartupjobs.com"
  lazy val maxDeepth = 2

  def jsoupBrowser = () => JsoupBrowser()
  def htmlBrowser = () => HtmlUnitBrowser()
  def doc: Document = browser.get(rootUrl)

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

  //val f = doc >> elementList(".menu-item").map(_ >> text("li"))
  //println("=== f ===")
  //println(f)


  //val g = doc >> "menu-item menu-item-type-taxonomy menu-item-object-category menu-link" |> println
//  val aaa = doc >> elementList(".w-container menu-container")
//  println("AAA: "+aaa)
//  val aaa2 = doc.body >> elements(".w-container menu-container")
//  println("----")
//  println(aaa2)

  //val laaa = aaa2.map(_ >> text("a"))
  //println(laaa)

  //doc >> extractor(".logo img", attr("src")) |> println
  //doc >> extractorAt[String]("example-extractor") |> println

  println("==================")
  println("caralho")

  //doc >> ".small-news-list h4 > a" foreach println

}
