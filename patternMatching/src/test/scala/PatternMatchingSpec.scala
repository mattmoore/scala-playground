import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class PatternMatchingSpec extends AnyFreeSpec with Matchers {
  "pattern matching with case classes and wildcards" in {
    case class Company(val name: String)
    case class Person(val firstName: String, val lastName: String, company: Company)

    val person1 = Person("Matt", "Moore", Company("Rally"))
    val person2 = Person("James", "Jones", Company("Monster's Inc."))

    def matchPerson(person: Person) = person match {
      case Person(_, "Moore", Company(_)) => "You're a Rally employee!"
      case Person(_, "Jones", _)          => "Yo dawg!"
      case _                              => "No one matched!"
    }

    matchPerson(person1) shouldBe "You're a Rally employee!"
    matchPerson(person2) shouldBe "Yo dawg!"
  }

  "You should always construct with Option type instead of Some type because can't handle None in case match" in {
    val maybeValue = Option(1)
    val expected = 1
    val actual = maybeValue match {
      case Some(v) => v
      case None    => 0
    }
    actual shouldBe expected
  }

  "pattern matching on empty list" in {
    val list = List.empty[Int]
    val expected = 0
    val actual = list match {
      case x :: xs => 1
      case Nil     => 0
    }
    actual shouldBe expected
  }

  "pattern matching on list with multiple items" in {
    val list = List(1, 2)
    val expected = 1
    val actual = list match {
      case x :: xs => 1
      case Nil     => 0
    }
    actual shouldBe expected
  }

  "pattern matching on list with one item" in {
    val list = List(2)
    val expected = 1
    val actual = list match {
      case x :: xs => 1
      case Nil     => 0
    }
    actual shouldBe expected
  }

  "pattern matching with case to match on list with at least one item and at most one item" in {
    val list = List(2)
    val expected = 1
    val actual = list match {
      case x :: Nil => 1
      case x :: xs  => 2
      case Nil      => 0
    }
    actual shouldBe expected
  }

  "pattern matching with case on list with at least one item and at most one item should handle multiple items as well" in {
    val list = List(1, 2)
    val expected = 2
    val actual = list match {
      case x :: Nil => 1
      case x :: xs  => 2
      case Nil      => 0
    }
    actual shouldBe expected
  }

  "Option[LocalDate] when Some with match" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2021, 1, 2)

    val expected = true
    val actual = !merchantStartDate.isAfter(txnUseDate) && (merchantStopDate match {
      case Some(d) => !d.isBefore(txnUseDate)
      case None    => true
    })
    actual shouldBe expected
  }

  "Option[LocalDate] when None using match" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    val expected = true
    val actual = !merchantStartDate.isAfter(txnUseDate) && (merchantStopDate match {
      case Some(d) => !d.isBefore(txnUseDate)
      case None    => true
    })
    actual shouldBe expected
  }

  "Option[LocalDate] when None using ||" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    val expected = true
    val actual = !merchantStartDate.isAfter(txnUseDate) &&
      (merchantStopDate.isEmpty || !merchantStopDate.get.isBefore(txnUseDate))
    actual shouldBe expected
  }

  "Option[LocalDate] when Some using fold" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2021, 1, 2)

    val expected = true
    val actual = !merchantStartDate.isAfter(txnUseDate) &&
      merchantStopDate.fold(true)(d => !d.isBefore(txnUseDate))
    actual shouldBe expected
  }

  "Option[LocalDate] when None using fold" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = None
    val txnUseDate = LocalDate.of(2021, 1, 2)

    val expected = true
    val actual = !merchantStartDate.isAfter(txnUseDate) && merchantStopDate.fold(true)(!_.isBefore(txnUseDate))
    actual shouldBe expected
  }

  "Option[LocalDate] when Some using fold and trx happens after merchant stop date" in {
    val merchantStartDate = LocalDate.of(2021, 1, 1)
    val merchantStopDate: Option[LocalDate] = Some(LocalDate.of(2021, 1, 30))
    val txnUseDate = LocalDate.of(2022, 1, 2)

    val expected = false
    val actual = !merchantStartDate.isAfter(txnUseDate) && merchantStopDate.fold(true)(!_.isBefore(txnUseDate))
    actual shouldBe expected
  }

  "Match case class on traverse" in {
    case class Person(firstName: String, lastName: String)
    val people = List(
      Person("Matt", "Moore"),
      Person("John", "Doe")
    )
    val expected = List("Matt Moore", "John Doe")
    val actual = people.traverse { case Person(firstName, lastName) =>
      IO.pure(s"$firstName $lastName")
    }.unsafeRunSync()
    actual shouldBe expected
  }
}
