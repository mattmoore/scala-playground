import java.time._

object TimeExtensions {
  implicit class LocalDateExtensions(d: LocalDate) {
    def toZonedDateTime(zone: ZoneId): ZonedDateTime =
      ZonedDateTime.of(d, LocalTime.of(0, 0), zone)

    def toEpochMilli: Long =
      d.toZonedDateTime(ZoneId.of("UTC")).toEpochMilli
  }

  implicit class ZonedDateTimeExtensions(time: ZonedDateTime) {
    def toEpochMilli: Long =
      time.toInstant.toEpochMilli
  }

  implicit class LongDateExtensions(millis: Long) {
    def toLocalDate: LocalDate =
      LocalDate.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"))
  }
}
