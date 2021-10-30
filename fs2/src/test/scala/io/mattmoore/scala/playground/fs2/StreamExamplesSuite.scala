package io.mattmoore.scala.playground.fs2

import cats.effect.std.Queue
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.effect.{IO, Sync}
import cats.implicits._
import fs2.Stream
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration._

class StreamExamplesSuite extends AsyncFunSuite with AsyncIOSpec with Matchers {
  implicit def unsafeLogger[F[_]: Sync] = Slf4jLogger.getLogger[F]

  test("Effectful streams") {
    def getAge(name: String): IO[Int] = IO(name.length)
    Stream("John", "Paul", "George", "Ringo")
      .evalMap(getAge)
      .compile
      .toList
      .asserting(_ shouldBe List(4, 4, 6, 5))
  }

  test("Infinite streams") {
    Stream(1, 2, 3).repeat
      .take(5)
      .compile
      .toList shouldBe List(1, 2, 3, 1, 2)
  }

  test("Lift empty iterator into stream") {
    Stream
      .fromIterator[IO](Iterator.empty[Int], 1)
      .compile
      .toList
      .asserting(_ shouldBe List.empty[Int])
  }

  test("Lift iterator into stream") {
    Stream
      .fromIterator[IO](List(1, 2, 3).iterator, 1)
      .compile
      .toList
      .asserting(_ shouldBe List(1, 2, 3))
  }

  test("Stream.eval flatMapped (>>) into iterator") {
    (
      Stream.eval(Logger[IO].info("Yo!"))
        >> Stream.fromIterator[IO](List(1, 2, 3).iterator, 1)
    ).compile.toList
      .asserting(_ shouldBe List(1, 2, 3))
  }

  test("Stream.exec flatMapped (*>) into iterator - iterator does nothing") {
    (
      Stream.exec(Logger[IO].info("Yo!"))
        *> Stream.fromIterator[IO](List(1, 2, 3).iterator, 1)
      ).compile.toList
      .asserting(_ shouldBe List.empty)
  }

  test("Pipes are a way to create individual steps that can be run in a stream") {
    def pipe[F[_]: Sync](name: String): Stream[F, Int] => Stream[F, Int] =
      _.evalTap { index =>
        Sync[F].delay(
          println(s"Stage $name processing $index by ${Thread.currentThread().getName}")
        )
      }

    Stream
      .emits(1 to 100)
      .covary[IO]
      .through(pipe("A"))
      .through(pipe("B"))
      .through(pipe("C"))
      .compile
      .drain
      .asserting(_ shouldBe ())
  }

  test("Timer") {
    Stream
      .awakeEvery[IO](5.seconds)
      .take(2)
      .compile
      .toList
      .asserting(_.size shouldBe 2)
  }

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
