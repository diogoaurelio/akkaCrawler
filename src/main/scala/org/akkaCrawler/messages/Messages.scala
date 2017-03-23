package org.akkaCrawler.messages

import akka.actor.ActorRef
import org.joda.time.LocalDateTime

import scala.concurrent.duration.{Duration, Deadline}

/**
  * Define inter actor communication messages
  * for protocol implementation
  */
object Messages {

  sealed class Result

  /**
    * conclusion state of work developed by actors
    */
  sealed class Failure(task: Task, msg: String, duration: Duration) extends Result

  /**
    * conclusion state of work developed by actors
    */
  sealed class Success(task: Task, msg: String, duration: Duration) extends Result

  /**
    * conclusion state of work developed by actors
    */
  sealed class Incomplete(task: Task, msg: String, duration: Duration) extends Result

  /**
    * conclusion state of work developed by actors
    */
  sealed class TimedOut(task: Task, msg: String, duration: Duration) extends Result

  /**
    * Generic work
    */
  sealed class Work

  /**
    * A generic website crawling job
    * @param rootUrl
    * @param id
    */

  case class Job(rootUrl: String, id: String) extends Work

  /**
    * A sub-task of job, which can be paralized among different workers
    * @param job
    * @param url
    * @param spiderName
    * @param timeout
    */
  case class Task(job: Job, url: String, spiderName: String, timeout: Duration) extends Work

  case class CoordinationWorkCorruption(msg: String) extends Work

  /**
    * Work request used by SpiderMaster
    */
  sealed class CommandRequest

  /**
    * Message sent to SpiderMaster to initiate crawling of full website
    * @param job
    */
  case class StartJob(job: Job) extends CommandRequest

  /**
    * Message sent to SpiderWork to start crawling a given website portion
    * Normal function should require SpiderWorker to reply with a WorkAccepted
    * message
    * @param task
    */
  case class StartCrawl(task: Task) extends CommandRequest

  /**
    * Message sent to SpiderWork to retrieve last task Result
    */
  case class QueryLastTaskResult() extends CommandRequest

  /**
    * SpiderMaster repeats requesting for work acknowledgement from SpiderWorker
    * to actor to confirm worker started job
    * in case of message delivery failure
    * @param retries
    * @param worker
    * @param task
    */
  case class AckTick(retries: Int, worker: ActorRef, task: Task)


  /**
    * Work response used by SpiderWorkers
    */
  sealed class CommandResponse

  case class WorkAccepted(task: Task, startTime: LocalDateTime) extends CommandResponse

  case class WorkRejected(workerStatus: WorkerStatus, msg: String)

  case class LastTaskResult(result: Option[Result])

  /**
    * Messages to allow SpiderMaster to monitor
    * SpiderWorkerStatus
    */
  sealed class WorkerStatus

  /**
    * SpiderWorker has no task running
    */
  case class Idle() extends WorkerStatus

  /**
    * SpiderWorker is running a given task
    * @param task
    * @param startTime
    */
  case class Busy(task: Work, startTime: LocalDateTime) extends WorkerStatus


}
