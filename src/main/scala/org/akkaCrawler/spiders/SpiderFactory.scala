package org.akkaCrawler.spiders

import org.akkaCrawler.drivers.{SpiderDriver, SpiderDriverCompanionObject}

import scala.concurrent.ExecutionContext


/**
  * Get Spider classes from config
  */
case class SpiderFactory(spiderClassName: String) {

  def spiderFor[T <: Spider](implicit ec: ExecutionContext): SpiderCompanionObject[T] = try {
    val spiderCompanionObjectClass = Class.forName(spiderClassName + "$")

    val spiderCompanionObjectConstructor = spiderCompanionObjectClass.getDeclaredConstructor()
    spiderCompanionObjectConstructor.setAccessible(true)
    spiderCompanionObjectConstructor.newInstance().asInstanceOf[SpiderCompanionObject[T]]

  } catch {
    case t: Throwable => throw new IllegalArgumentException(s"Could not instantiate Spider class ${spiderClassName}", t)
  }

  def driverFor[T <: Spider](url: String, spiderName: String)(implicit ec: ExecutionContext): SpiderDriver[T] = try {
    val driverCompanionObjectClass = Class.forName(spiderClassName + "driver$")

    val driverCompanionObjectConstructor = driverCompanionObjectClass.getDeclaredConstructor()
    driverCompanionObjectConstructor.setAccessible(true)
    val driverCompanionObject = driverCompanionObjectConstructor.newInstance().asInstanceOf[SpiderDriverCompanionObject[T]]
    driverCompanionObject(url, spiderName)

  } catch {
    case t: Throwable => throw new IllegalArgumentException(s"Could not instantiate driver class ${spiderClassName}", t)
  }
}