package org.akkaCrawler.spiders

import org.akkaCrawler.drivers.{SpiderDriver, SpiderDriverCompanionObject}

/**
  * Spider launcher
  */
case class SpiderFactory[T <: Spider](spiderName: String) {

  def driverFor[T <: Spider](driverClassName: String): SpiderDriver[T] = try {
    val driverCompanionObjectClass = Class.forName(driverClassName + "$")

    val driverCompanionObjectConstructor = driverCompanionObjectClass.getDeclaredConstructor()
    driverCompanionObjectConstructor.setAccessible(true)
    val driverCompanionObject = driverCompanionObjectConstructor.newInstance().asInstanceOf[SpiderDriverCompanionObject[T]]()
    driverCompanionObject
  } catch {
    case t: Throwable => throw new IllegalArgumentException(s"Could not instantiate driver class ${driverClassName}", t)
  }
}