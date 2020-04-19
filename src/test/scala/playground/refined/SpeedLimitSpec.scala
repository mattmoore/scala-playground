package playground.refined

import eu.timepit.refined._
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SpeedLimitSpec extends AnyFunSpec with Matchers {
  describe("Type refinements applied to speed limit enforcement") {
    type ValidSpeedLimit = Int Refined LessEqual[60]
    object ValidSpeedLimit extends RefinedTypeOps[ValidSpeedLimit, Int]

    describe("when speed limit is valid") {
      it("succeeds, returning Right") {
        val result: Either[String, ValidSpeedLimit] = ValidSpeedLimit.from(60)
        result.map(refined => refined.value) shouldBe Right(60)
      }

      it("fails, returning Left") {
        val result: Either[String, ValidSpeedLimit] = ValidSpeedLimit.from(61)
        result.map(refined => refined.value) shouldBe Left("Predicate (61 > 60) did not fail.")
      }
    }
  }
}
