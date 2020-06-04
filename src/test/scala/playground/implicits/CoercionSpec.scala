package playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CoercionSpec extends AnyFunSpec with Matchers {
  describe("type coercion via implicits for string to int conversion") {
    implicit def stringToInt(string: String) = string.toInt

    it("converts a string to a number, based on matching types with stringToInt") {
      val x: Int = "5"
      x shouldBe 5
    }
  }

  describe("type coercion via implicit for loading a person record") {
    case class User(val id: Int, val username: String)
    implicit def loadUser(userId: Int): User = User(userId, "mattmoore")

    it("can load a user for a given user ID") {
      val user: User = 1
      user shouldBe User(1, "mattmoore")
    }
  }
}
