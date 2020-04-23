package playground.catamorphism

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ListFoldSpec extends AnyFunSpec with Matchers {
  describe("combines list of numbers via an operation") {
    def sum =
      List(1, 2, 3).fold(0)((a, b) => a + b)

    def sumShorthand =
      List(1, 2, 3).fold(0)(_ + _)

    it("should sum the numbers") {
      sum shouldBe 6
      sumShorthand shouldBe 6
    }
  }
}
