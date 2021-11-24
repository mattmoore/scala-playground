import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class PersonSpec extends AnyFunSpec with Matchers {
  describe("Person class with apply") {
    case class Person(id: UUID, firstName: String, lastName: String)

    object Person {
      def apply(id: UUID, firstName: String, lastName: String): Person =
        new Person(id, firstName, lastName)

      def unapply(p: Person): (UUID, String, String) =
        (p.id, p.firstName, p.lastName)
    }

    it("should construct a Person with apply") {
      val person = Person.apply(
        UUID.fromString("739d9b2f-6f58-4cd0-a622-a4721030d4ee"),
        firstName = "Matt",
        lastName = "Moore"
      )
      person shouldBe new Person(
        id = UUID.fromString("739d9b2f-6f58-4cd0-a622-a4721030d4ee"),
        firstName = "Matt",
        lastName = "Moore"
      )
    }

    it("should construct a tuple with unapply") {
      val person = Person(
        UUID.fromString("739d9b2f-6f58-4cd0-a622-a4721030d4ee"),
        firstName = "Matt",
        lastName = "Moore"
      )
      Person.unapply(person) shouldBe (
        UUID.fromString("739d9b2f-6f58-4cd0-a622-a4721030d4ee"),
        "Matt",
        "Moore"
      )
    }
  }
}
