package org.akkaCrawler.messages

/**
  * Define inter actor communication messages
  * for protocol implementation
  */
object Messages {

  /**
    * Work request
    */
  sealed class CommandRequest

  /**
    * conclusion state of work developed by actors
    */
  sealed class Failure

  /**
    * conclusion state of work developed by actors
    */
  sealed class Success

  case class StartCrawl(url: String, spiderName: String) extends CommandRequest

}
