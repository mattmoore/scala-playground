import TimeExtensions._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.time._

class TimeExtensionsSpec extends AnyFunSpec with Matchers {
  describe("LocalDateExtensions") {
    describe("toZonedDateTime") {
      val zone = ZoneId.of("UTC")
      val date = LocalDate.of(2021, 11, 1)

      it("transforms LocalDate to ZonedDateTime, based on timezone") {
        val expected = ZonedDateTime.of(2021, 11, 1, 0, 0, 0, 0, zone)
        val actual = date.toZonedDateTime(zone)
        actual shouldBe expected
      }
    }

    describe("toEpicMilli") {
      val date = LocalDate.of(2021, 11, 1)

      it("transforms LocalDate to UTC epoch millis") {
        val expected = 1635724800000L
        val actual = date.toEpochMilli
        actual shouldBe expected
      }
    }
  }

  describe("ZonedDateTimeExtensions") {
    describe("toEpicMilli") {
      val zone = ZoneId.of("UTC")
      val date = ZonedDateTime.of(2021, 11, 1, 0, 0, 0, 0, zone)

      it("transforms ZonedDateTime to UTC epoch millis") {
        val expected = 1635724800000L
        val actual = date.toEpochMilli
        actual shouldBe expected
      }
    }
  }

  describe("LongDateExtensions") {
    describe("toLocalDate") {
      val millis = 1635724800000L

      it("transforms Long millis to LocalDate, in UTC") {
        val expected = LocalDate.of(2021, 11, 1)
        val actual = millis.toLocalDate
        actual shouldBe expected
      }
    }
  }
}
