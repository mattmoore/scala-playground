package io.mattmoore.scala.playground.refinementTypes

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import eu.timepit.refined._
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

class PositiveIntSpec extends AnyFunSpec with Matchers {
  describe("Integer domain restriction") {
    describe("using Int Refined Positive built-in refinement type") {
      describe("when passed a positive integer") {
        it("compiles successfully") {
          val i1: Int Refined Positive = 5
          i1.value shouldEqual 5
        }
      }
    }

    describe("using type inference") {
      describe("with refineMV") {
        it("restricts integers to positives, while inferring the value type") {
          refineMV[Positive](5).value shouldBe 5
        }
      }

      describe("with refineV") {
        it("behaves the same as refineMV, but returns an Either monad") {
          val result = refineV[Positive](5)
          result.map(refined => refined.value) shouldBe Right(5)
        }
      }
    }

    describe("using PositiveInt custom type") {
      type PositiveInt = Int Refined Positive
      object PositiveInt extends RefinedTypeOps[PositiveInt, Int]

      it("allows us to define our own types with predicates") {
        val result: Either[String, PositiveInt] = PositiveInt.from(5)
        result.map(refined => refined.value) shouldBe Right(5)
      }
    }
  }
}
