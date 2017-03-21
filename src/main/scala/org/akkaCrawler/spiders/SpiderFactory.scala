package org.akkaCrawler.spiders


/**
  * Get Spider classes from config
  */
case class SpiderFactory(spiderClassName: String) {

  def spiderFor[T <: Spider]: SpiderCompanionObject[T] = try {
    val spiderCompanionObjectClass = Class.forName(spiderClassName + "$")

    val spiderCompanionObjectConstructor = spiderCompanionObjectClass.getDeclaredConstructor()
    spiderCompanionObjectConstructor.setAccessible(true)
    spiderCompanionObjectConstructor.newInstance().asInstanceOf[SpiderCompanionObject[T]]

  } catch {
    case t: Throwable => throw new IllegalArgumentException(s"Could not instantiate driver class ${spiderClassName}", t)
  }
}