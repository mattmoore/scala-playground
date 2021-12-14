package db.postgres

import domain._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._

import java.util.UUID

object Queries {
  def selectTransaction(id: UUID): Query0[Transaction] =
    sql"""SELECT *
         |FROM transactions
         |WHERE id = $id
         |""".stripMargin.query

  def insertTransaction(t: Transaction): Update0 =
    sql"""INSERT INTO transactions (
         |  date
         |) VALUES (
         |  ${t.date}
         |)
         |"""
      .stripMargin
      .update

  def updateTransaction(t: Transaction): Update0 =
    sql"""UPDATE transactions SET
         |  date = ${t.date}
         |WHERE id = ${t.id}
         |"""
      .stripMargin
      .update
}
