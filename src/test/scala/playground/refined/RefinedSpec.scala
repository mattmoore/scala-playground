package playground.refined

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

class RefinedSpec extends AnyFlatSpec with Matchers {
  "Int Refined Positive" should "restrict integers to positives" in {
    val i1: Int Refined Positive = 5
    i1.value shouldEqual 5
  }
}
