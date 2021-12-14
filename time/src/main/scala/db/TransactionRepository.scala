package db

import algebras._
import cats.effect._
import postgres._
import domain._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._

import java.util.UUID

class TransactionRepository[F[_]: Async](transactor: Transactor[F]) extends RepositoryAlgebra[F, Transaction] {
  override def get(id: UUID): F[Transaction] =
    Queries
      .selectTransaction(id)
      .unique
      .transact(transactor)

  override def add(t: Transaction): F[UUID] =
    Queries
      .insertTransaction(t)
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(transactor)

  override def update(t: Transaction): F[UUID] =
    Queries
      .updateTransaction(t)
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(transactor)
}
