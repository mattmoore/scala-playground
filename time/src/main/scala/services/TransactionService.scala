package services

import algebras._
import db.TransactionRepository
import domain.Transaction

import java.util.UUID

class TransactionService[F[_]](repo: TransactionRepository[F]) extends TransactionServiceAlgebra[F] {
  override def get(id: UUID): F[Transaction] =
    repo.get(id)

  override def add(t: Transaction): F[UUID] =
    repo.add(t)

  override def update(t: Transaction): F[UUID] =
    repo.update(t)
}
