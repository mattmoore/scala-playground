package domain

import java.time._
import java.util.UUID

case class Transaction(
  id: Option[UUID] = None,
  date: ZonedDateTime
)
