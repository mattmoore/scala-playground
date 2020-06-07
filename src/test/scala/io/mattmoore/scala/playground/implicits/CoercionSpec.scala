package io.mattmoore.scala.playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CoercionSpec extends AnyFunSpec with Matchers {

  case class User(val id: Int, val username: String)

  val users = List(
    User(1, "mattmoore"),
    User(2, "janedoe")
  )

  // Defining these implicit functions above the describe blocks to demonstrate that multiple implicit functions can be defined.
 
  implicit def stringToInt(string: String) = string.toInt

  implicit def loadUser(userId: Int): User = users.find(_.id == userId).getOrElse(null)

  implicit def loadUserOption(userId: Int): Option[User] = Option(loadUser(userId))

  implicit def loadUserEither(userId: Int): Either[String, User] = loadUserOption(userId).toRight(s"Unable to load user $userId.")

  describe("type coercion via implicits for string to int conversion") {
    it("converts a string to a number, based on matching types with stringToInt") {
      val x: Int = "5"
      x shouldBe 5
    }
  }

  describe("type coercion via implicit for loading a person record") {
    it("can load a user for the given user ID") {
      val user: User = 1
      user shouldBe User(1, "mattmoore")
    }
  }

  describe("type coercion via implicit for loading a person record, wrapped in Option") {
    describe("for a valid user") {
      it("can load a user for a given user ID") {
        val user: Option[User] = 1
        user shouldBe Some(User(1, "mattmoore"))
      }
    }

    describe("for an invalid user") {
      it("will return None") {
        val user: Option[User] = 0
        user shouldBe None
      }
    }
  }

  describe("type coercion via implicit for loading a person record, wrapped in Either") {
    describe("for a valid user") {
      it("can load a user for a given user ID") {
        val user: Either[String, User] = 1
        user shouldBe Right(User(1, "mattmoore"))
      }
    }

    describe("for an invalid user") {
      it("will return an error message wrapped in Left") {
        val user: Either[String, User] = 0
        user shouldBe Left("Unable to load user 0.")
      }
    }
  }
}
