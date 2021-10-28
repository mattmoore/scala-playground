package io.mattmoore.scala.playground.fs2

import cats.effect.{IO, Sync}
import cats.effect.std.Queue
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits._
import fs2.Stream
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration._

class StreamExamplesSuite extends AsyncFunSuite with AsyncIOSpec with Matchers {
  test("Creating a queue") {
    val program = for {
      queue <- Queue.unbounded[IO, Option[Int]]
      streamFromQueue = Stream.fromQueueNoneTerminated(queue)
      _ <- Seq(Some(1), Some(2), Some(3), None).map(queue.offer).sequence
      result <- streamFromQueue.compile.toList
    } yield result
    program.asserting(_ shouldBe List(1, 2, 3))
  }

  test("Effectful streams") {
    def getAge(name: String): IO[Int] = IO(name.length)
    val stream = Stream("John", "Paul", "George", "Ringo")
    stream.evalMap(getAge).compile.toList.asserting(_ shouldBe List(4, 4, 6, 5))
  }

  test("Infinite streams") {
    val stream = Stream(1, 2, 3).repeat
    stream.take(5).compile.toList shouldBe List(1, 2, 3, 1, 2)
  }

  test("Lift empty iterator into stream") {
    Stream.fromIterator[IO](Iterator.empty[Int], 1).compile.toList.asserting(_ shouldBe List.empty[Int])
  }

  test("Lift iterator into stream") {
    Stream.fromIterator[IO](List(1, 2, 3).iterator, 1).compile.toList.asserting(_ shouldBe List(1, 2, 3))
  }

  test("Pipes are a way to create individual steps that can be run in a stream") {
    def pipe[F[_] : Sync](name: String): Stream[F, Int] => Stream[F, Int] =
      _.evalTap { index =>
        Sync[F].delay(
          println(s"Stage $name processing $index by ${Thread.currentThread().getName}")
        )
      }

    Stream.emits(1 to 10000)
      .covary[IO]
      .through(pipe("A"))
      .through(pipe("B"))
      .through(pipe("C"))
      .compile
      .drain
      .asserting(_ shouldBe ())
  }

  test("Timer") {
    val stream = Stream.awakeEvery[IO](5.seconds)
    stream.take(2).compile.toList.asserting(_.size shouldBe 2)
  }
}
