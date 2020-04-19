package playground.refined

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import eu.timepit.refined._
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

class RefinedSpec extends AnyFlatSpec with Matchers {
  "Int Refined Positive" should "restrict integers to positives" in {
    val i1: Int Refined Positive = 5
    i1.value shouldEqual 5
  }

  "refineMV" should "restrict integers to positives, while inferring the value type" in {
    refineMV[Positive](5).value shouldBe 5
  }

  "refineV" should "behave the same as refineMV, but return an Either monad" in {
    val result = refineV[Positive](5)
    result.map(refined => refined.value) shouldBe Right(5)
  }

  "Custom refined type" should "allow us to define our own types with predicates" in {
    type PositiveInt = Int Refined Positive
    object PositiveInt extends RefinedTypeOps[PositiveInt, Int]
    val result: Either[String, PositiveInt] = PositiveInt.from(5)
    result.map(refined => refined.value) shouldBe Right(5)
  }
}
