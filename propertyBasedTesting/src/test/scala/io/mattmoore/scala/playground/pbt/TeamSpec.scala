package io.mattmoore.scala.playground.pbt

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop.forAll
import org.scalacheck._

object TeamSpec extends Properties("Team") {
  val genTeam: Gen[Team] = for {
    name <- arbitrary[String]
    managerName <- arbitrary[String]
    yearFounded <- Gen.choose(1900, 2021)
  } yield Team(name, managerName, yearFounded)

  implicit lazy val arbitraryTeam: Arbitrary[Team] = Arbitrary(genTeam)

  property("yearFounded") = forAll { t: Team =>
    t.yearFounded >= 1900 && t.yearFounded <= 2021
  }
}
