package io.mattmoore.scala.playground.catamorphisms

import cats.data._
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._
import fs2._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RunningState extends AnyFunSuite with Matchers {
  type Balances = Map[String, BigDecimal]

  case class Transaction(name: String, amount: BigDecimal)

  val initialBalances = Map(
    "Alice" -> BigDecimal(100),
    "Bob" -> BigDecimal(200)
  )

  val transactions = Vector(
    Transaction("Alice", 50),
    Transaction("Alice", 50),
    Transaction("Bob", 50),
    Transaction("Bob", 50)
  )

  def updateBalancesFold(
    balances: Balances,
    transactions: Vector[Transaction]
  ): Map[String, BigDecimal] =
    transactions.foldLeft(balances) { case (balances, transaction) =>
      balances + (transaction.name -> (balances(transaction.name) - transaction.amount))
    }

  test("Using fold, we get the final balance result, but do not keep the history of transactions in the output.") {
    val result = updateBalancesFold(initialBalances, transactions)
    result shouldBe Map("Alice" -> 0, "Bob" -> 100)
  }

  def updateBalancesFoldWithTransactions(
    balances: Balances,
    transactions: Vector[Transaction]
  ): (Balances, Vector[Transaction]) =
    transactions.foldLeft((balances, Vector.empty[Transaction])) { case ((balances, transactions), transaction) =>
      val updatedBalances = balances + (transaction.name -> (balances(transaction.name) - transaction.amount))
      val updatedTransactions = transactions :+ transaction
      (updatedBalances, updatedTransactions)
    }

  test("Another fold where we get the final balance result and also keep the history of transactions in the output.") {
    val result = updateBalancesFoldWithTransactions(initialBalances, transactions)
    result shouldBe (
      Map("Alice" -> 0, "Bob" -> 100),
      Vector(
        Transaction("Alice", 50),
        Transaction("Alice", 50),
        Transaction("Bob", 50),
        Transaction("Bob", 50)
      )
    )
  }

  def updateBalancesStateMonad(
    transactions: Vector[Transaction]
  ): State[Balances, Vector[Transaction]] =
    transactions.traverse { transaction =>
      State { balances =>
        val updatedBalances = balances + (transaction.name -> (balances(transaction.name) - transaction.amount))
        (updatedBalances, transaction)
      }
    }

  test("Using State monad, we get the final balance calculation, while also maintaining transaction history.") {
    val result = updateBalancesStateMonad(transactions).run(initialBalances).value
    result shouldBe (Map("Alice" -> 0, "Bob" -> 100), transactions)
  }

  def updateBalancesStream[F[_]](
    initialBalances: Balances,
    transactions: Stream[F, Transaction]
  ): Stream[F, (Balances, Transaction)] =
    transactions.mapAccumulate(initialBalances) { case (balances, transaction) =>
      val updatedBalances = balances + (transaction.name -> (balances(transaction.name) - transaction.amount))
      (updatedBalances, transaction)
    }

  test("Using fs2.Stream.") {
    val result = updateBalancesStream(initialBalances, Stream.emits(transactions)).compile.toVector
    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), Transaction("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), Transaction("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), Transaction("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), Transaction("Bob", 50))
    )
  }

  test("Using fs2.Stream, lifted to IO.") {
    val result = updateBalancesStream[IO](initialBalances, Stream.emits(transactions)).compile.toVector.unsafeRunSync
    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), Transaction("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), Transaction("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), Transaction("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), Transaction("Bob", 50))
    )
  }

  // TODO: Add computation to this example.
  test("Multiple streams feeding into balance calculations.") {
    val initialBalances = Map(
      "Alice" -> 100,
      "Bob" -> 200,
      "Mary" -> 200,
      "John" -> 100
    )
    val transactionsA = Vector(
      Transaction("Alice", BigDecimal(50)),
      Transaction("Alice", BigDecimal(50)),
      Transaction("Bob", BigDecimal(50)),
      Transaction("Bob", BigDecimal(50))
    )
    val transactionsB = Vector(
      Transaction("Mary", BigDecimal(50)),
      Transaction("Mary", BigDecimal(50)),
      Transaction("John", BigDecimal(50)),
      Transaction("John", BigDecimal(50))
    )
    val transactionsC = Vector(
      Transaction("Anthony", BigDecimal(50)),
      Transaction("Anthony", BigDecimal(50)),
      Transaction("Sarah", BigDecimal(50)),
      Transaction("Sarah", BigDecimal(50))
    )

    val streamA = Stream.emits(transactionsA)
    val streamB = Stream.emits(transactionsB)
    val streamC = Stream.emits(transactionsC)

    val combinedStream = Stream(streamA, streamB, streamC).flatten

    val result = combinedStream.compile.toVector
    println(result)
  }
}
