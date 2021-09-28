package io.mattmoore.scala.playground.pbt

import org.scalacheck.Prop._
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck._

object TeamShapelessSpec extends Properties("Team") {
  property("yearFounded") = forAll { t: Team =>
    passed
  }
}
