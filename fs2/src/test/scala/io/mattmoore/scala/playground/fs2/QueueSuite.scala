package io.mattmoore.scala.playground.fs2

import cats.effect.IO
import cats.effect.std.Queue
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits._
import fs2.Stream
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

class QueueSuite extends AsyncFunSuite with AsyncIOSpec with Matchers {
  test("Creating a queue") {
    val program = for {
      queue <- Queue.unbounded[IO, Option[Int]]
      streamFromQueue = Stream.fromQueueNoneTerminated(queue)
      _ <- Seq(Some(1), Some(2), Some(3), None).map(queue.offer).sequence
      result <- streamFromQueue.compile.toList
    } yield result
    program.asserting(_ shouldBe List(1, 2, 3))
  }
}
