package algebras

import domain._

import java.util.UUID

trait TransactionServiceAlgebra[F[_]] {
  def get(id: UUID): F[Transaction]
  def add(t: Transaction): F[UUID]
  def update(t: Transaction): F[UUID]
}
