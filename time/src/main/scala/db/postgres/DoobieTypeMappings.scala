//package db.postgres
//
//import doobie.Meta
//
//import java.time.{Instant, ZonedDateTime}
//
//object DoobieTypeMappings {
//  implicit val InstantMeta: Meta[ZonedDateTime] =
//    Meta[ZonedDateTime].imap(_.to)
//}
