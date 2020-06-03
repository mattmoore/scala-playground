package playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CoercionSpec extends AnyFunSpec with Matchers {
  describe("coercion via implicits") {
    implicit def stringToInt(string: String) = string.toInt

    it("can automatically convert string to a number, based on matching types with stringToInt") {
      val x: Int = "5"
      x shouldBe 5
    }
  }
}
