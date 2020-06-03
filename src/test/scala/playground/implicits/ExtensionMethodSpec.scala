package playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ExtensionMethodSpec extends AnyFunSpec with Matchers {
  describe("extension methods via implicits") {
    class RichInt(i: Int) {
      def square = i * i
    }

    implicit def richInt(i: Int) = new RichInt(i)

    it("makes int have a square method") {
      2.square shouldBe 4
    }
  }
}
