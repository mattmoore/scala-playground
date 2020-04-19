package playground.refined

import eu.timepit.refined._
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SpeedLimitSpec extends AnyFunSpec with Matchers {
  // Define refined type to represent speeds that fall at or below the legal speed limit of 60
  type LegalSpeedLimit = Int Refined LessEqual[60]
  object LegalSpeedLimit extends RefinedTypeOps[LegalSpeedLimit, Int]
  // Define refined type to represent speeds that fall above the legal speed limit of 60
  type IllegalSpeedLimit = Int Refined Greater[60]
  object IllegalSpeedLimit extends RefinedTypeOps[IllegalSpeedLimit, Int]

  describe("Refinement types applied to speed limit enforcement") {
    describe("when under the speed limit") {
      it("succeeds, returning Right") {
        LegalSpeedLimit.from(60).map(refined => refined.value) shouldBe Right(60)
      }
    }

    describe("when over the speed limit") {
      it("fails, returning Left") {
        LegalSpeedLimit.from(61).map(refined => refined.value) shouldBe Left("Predicate (61 > 60) did not fail.")
      }
    }
  }

  describe("Combine refinement types with pattern matching to issue a speeding ticket.") {
    def checkSpeed(speed: Int) = speed match {
      case LegalSpeedLimit(speed) => "You're fine. Have a good day, citizen!"
      case IllegalSpeedLimit(speed) => "You're speeding! Ticket issued!"
    }

    describe("when speed limit is under the limit") {
      it("does nothing, because the driver is obeying the limit") {
        checkSpeed(60) shouldBe "You're fine. Have a good day, citizen!"
      }
    }

    describe("when speed limit is over the limit") {
      it("issues a ticket") {
        checkSpeed(61) shouldBe "You're speeding! Ticket issued!"
      }
    }
  }
}
