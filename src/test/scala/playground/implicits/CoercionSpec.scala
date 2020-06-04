package playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CoercionSpec extends AnyFunSpec with Matchers {
  case class User(val id: Int, val username: String)
  val users = List(
    User(1, "mattmoore"),
    User(2, "janedoe")
  )

  describe("type coercion via implicits for string to int conversion") {
    implicit def stringToInt(string: String) =
      string.toInt
    val x: Int = "5"

    it("converts a string to a number, based on matching types with stringToInt") {
      x shouldBe 5
    }
  }

  describe("type coercion via implicit for loading a person record") {
    implicit def loadUser(userId: Int): User = users.find(_.id == userId).get

    val user: User = 1

    it("can load a user for the given user ID") {
      user shouldBe User(1, "mattmoore")
    }
  }

  describe("type coercion via implicit for loading a person record, wrapped in Option") {
    implicit def loadUser(userId: Int): Option[User] = users.find(_.id == userId)

    val user: Option[User] = 1

    it("can load a user for a given user ID") {
      user shouldBe Some(User(1, "mattmoore"))
    }
  }

  describe("type coercion via implicit for loading a person record, wrapped in Either") {
    implicit def loadUser(userId: Int): Either[String, User] =
      users.find(_.id == userId).toRight(s"Unable to load user $userId.")

    describe("when a valid user is found") {
      val user: Either[String, User] = 1

      it("can load a user for a given user ID") {
        user shouldBe Right(User(1, "mattmoore"))
      }
    }

    describe("when a user cannot be found") {
      val user: Either[String, User] = 0

      it("will return an error message wrapped in Left") {
        user shouldBe Left("Unable to load user 0.")
      }
    }
  }
}
