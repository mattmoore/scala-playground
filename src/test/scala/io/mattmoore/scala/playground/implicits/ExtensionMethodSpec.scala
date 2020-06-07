package io.mattmoore.scala.playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ExtensionMethodSpec extends AnyFunSpec with Matchers {
  describe("extension methods via implicits") {
    implicit class RichInt(i: Int) {
      def square = i * i
    }

    it("makes int have a square method") {
      2.square shouldBe 4
    }
  }
}
