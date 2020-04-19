package playground.refined

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
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

  describe("Refinement types + pattern matching to determine whether to issue a speeding ticket.") {
    def checkSpeed(speed: Int) = speed match {
      case LegalSpeedLimit(speed) => s"Your speed is $speed. You're fine. Have a good day, citizen!"
      case IllegalSpeedLimit(speed) => s"Your speed is $speed. You're speeding! Ticket issued!"
    }

    describe("when speed limit is under the limit") {
      it("does nothing, because the driver is obeying the limit") {
        checkSpeed(60) shouldBe "Your speed is 60. You're fine. Have a good day, citizen!"
      }
    }

    describe("when speed limit is over the limit") {
      it("issues a ticket") {
        checkSpeed(61) shouldBe "Your speed is 61. You're speeding! Ticket issued!"
      }
    }
  }
}
