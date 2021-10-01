package io.mattmoore.scala.playground.cats

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

import java.util.concurrent.TimeoutException

class CatsSuite extends AsyncFunSuite with AsyncIOSpec with Matchers {
  test("IO describes a computation") {
    IO(1).asserting(_ shouldBe 1)
  }

  test("IO race returns the winner and cancels the loser") {
    def first = IO.sleep(1.second) *> IO("first")
    def second = IO("second")
    IO.race(first, second).asserting(_ shouldBe Right("second"))
  }

  test("IO timeout fails with TimeoutException if not complete within duration") {
    def f = IO.sleep(2.seconds) *> IO("f")
    f.timeout(1.second).assertThrows[TimeoutException]
  }

  test("IO timeoutTo executes fallbackIO if io is not complete within duration") {
    def f = IO.sleep(2.seconds) *> IO("f")
    def g = IO("g")
    f.timeoutTo(1.seconds, g).asserting(_ shouldBe "g")
  }
}
