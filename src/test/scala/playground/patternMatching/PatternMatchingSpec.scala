package playground.patternMatching

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PatternMatchingSpec extends AnyFunSpec with Matchers {
  describe("pattern matching example") {
    case class Company(val name: String)
    case class Person(val firstName: String, val lastName: String, company: Company)

    val person1 = Person("Matt", "Moore", Company("Rally"))
    val person2 = Person("James", "Jones", Company("Monster's Inc."))

    def matchPerson(person: Person) = person match {
      case Person(_, "Moore", Company(_)) => "You're a Rally employee!"
      case Person(_, "Jones", _) => "Yo dawg!"
      case _ => "No one matched!"
    }

    it("matches the correct person") {
      matchPerson(person1) shouldBe "You're a Rally employee!"
      matchPerson(person2) shouldBe "Yo dawg!"
    }
  }
}
