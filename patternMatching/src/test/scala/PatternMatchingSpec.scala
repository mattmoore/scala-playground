import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class PatternMatchingSpec extends AnyFunSpec with Matchers {
  describe("pattern matching example") {
    case class Company(val name: String)
    case class Person(val firstName: String, val lastName: String, company: Company)

    val person1 = Person("Matt", "Moore", Company("Rally"))
    val person2 = Person("James", "Jones", Company("Monster's Inc."))

    def matchPerson(person: Person) = person match {
      case Person(_, "Moore", Company(_)) => "You're a Rally employee!"
      case Person(_, "Jones", _)          => "Yo dawg!"
      case _                              => "No one matched!"
    }

    it("matches the correct person") {
      matchPerson(person1) shouldBe "You're a Rally employee!"
      matchPerson(person2) shouldBe "Yo dawg!"
    }
  }

  describe("Option[LocalDate] when Some with match") {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2021, 1, 2)

    it("handles Some correctly") {
      val expected = true
      val actual = !merchantStartDate.isAfter(txnUseDate) && (merchantStopDate match {
        case Some(d) => !d.isBefore(txnUseDate)
        case None    => true
      })
      actual shouldBe expected
    }
  }

  describe("Option[LocalDate] when None using match"){
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    it("handles None correctly") {
      val expected = true
      val actual = !merchantStartDate.isAfter(txnUseDate) && (merchantStopDate match {
        case Some(d) => !d.isBefore(txnUseDate)
        case None    => true
      })
      actual shouldBe expected
    }
  }

  describe("Option[LocalDate] when None using ||") {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    it("handles None correctly") {
      val expected = true
      val actual = !merchantStartDate.isAfter(txnUseDate) &&
        (merchantStopDate.isEmpty || !merchantStopDate.get.isBefore(txnUseDate))
      actual shouldBe expected
    }
  }

  describe("Option[LocalDate] when Some using fold") {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2021, 1, 2)

    it("handles None correctly") {
      val expected = true
      val actual = !merchantStartDate.isAfter(txnUseDate) &&
        merchantStopDate.fold(true)(d => !d.isBefore(txnUseDate))
      actual shouldBe expected
    }
  }

  describe("Option[LocalDate] when None using fold") {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    it("handles None correctly") {
      val expected = true
      val actual = !merchantStartDate.isAfter(txnUseDate) && merchantStopDate.fold(true)(!_.isBefore(txnUseDate))
      actual shouldBe expected
    }
  }

  describe("Option[LocalDate] when Some using fold and trx happens after merchant stop date") {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2022, 1, 2)

    it("handles None correctly") {
      val expected = false
      val actual = !merchantStartDate.isAfter(txnUseDate) && merchantStopDate.fold(true)(!_.isBefore(txnUseDate))
      actual shouldBe expected
    }
  }
}
