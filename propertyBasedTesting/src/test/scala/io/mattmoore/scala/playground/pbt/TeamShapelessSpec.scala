package io.mattmoore.scala.playground.pbt

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck._

object TeamShapelessSpec extends Properties("Team") {
  val genTeam: Gen[Team] = for {
    name <- arbitrary[String]
    managerName <- arbitrary[String]
    yearFounded <- Gen.choose(1900, 2021)
  } yield Team(name, managerName, yearFounded)

  property("yearFounded") = forAll(genTeam) { t: Team =>
    t.yearFounded >= 1900 && t.yearFounded <= 2021
  }
}
