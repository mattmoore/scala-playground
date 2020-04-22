package playground.catamorphism

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CatamorphismSpec extends AnyFunSpec with Matchers {
  describe("fold") {
    describe("combines list of numbers via an operation") {
      def combine =
        List(1, 2, 3).fold(0)(_ + _)

      it("should sum the numbers") {
        combine shouldBe 6
      }
    }

    describe("combines list of objects via an operation") {
      case class Person(val firstName: String, val lastName: String, val email: String)
      val people = List(
        Person("Matt", "Moore", "matt@mattmoore.io"),
        Person("Jane", "Doe", "jane@janedoe.io")
      )

      def transformEmails =
        people.foldLeft(List[String]())((a, b) => a :+ b.email)

      it("should transform the people list into an email list") {
        transformEmails shouldBe List("matt@mattmoore.io", "jane@janedoe.io")
      }
    }
  }
}
