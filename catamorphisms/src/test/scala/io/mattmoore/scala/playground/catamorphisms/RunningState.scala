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
  type Transaction = (String, BigDecimal)

  val initialBalances = Map(
    "Alice" -> BigDecimal(100),
    "Bob" -> BigDecimal(200)
  )

  val transactions = Vector(
    ("Alice", BigDecimal(50)),
    ("Alice", BigDecimal(50)),
    ("Bob", BigDecimal(50)),
    ("Bob", BigDecimal(50))
  )

  def updateBalancesFold(
    balances: Balances,
    transactions: Vector[Transaction]
  ): Map[String, BigDecimal] =
    transactions.foldLeft(balances) { case (bal, (name, amount)) =>
      bal + (name -> (bal(name) - amount))
    }

  test("Using fold, we get the final balance result, but do not keep the history of transactions in the output.") {
    val result = updateBalancesFold(initialBalances, transactions)
    result shouldBe Map("Alice" -> 0, "Bob" -> 100)
  }

  def updateBalancesFoldWithTransactions(
    balances: Balances,
    transactions: Vector[Transaction]
  ): (Balances, Vector[(String, BigDecimal)]) =
    transactions.foldLeft((balances, Vector.empty[Transaction])) { case ((bal, trxs), (name, amount)) =>
      val updatedBalances = bal + (name -> (bal(name) - amount))
      val updatedTransactions = trxs :+ (name, amount)
      (updatedBalances, updatedTransactions)
    }

  test("Another fold where we get the final balance result and also keep the history of transactions in the output.") {
    val result = updateBalancesFoldWithTransactions(initialBalances, transactions)
    result shouldBe (
      Map("Alice" -> 0, "Bob" -> 100),
      Vector(
        ("Alice", 50),
        ("Alice", 50),
        ("Bob", 50),
        ("Bob", 50)
      )
    )
  }

  def updateBalancesStateMonad(
    transactions: Vector[Transaction]
  ): State[Balances, Vector[Transaction]] =
    transactions.traverse { transaction =>
      State { balances =>
        val name = transaction._1
        val amount = transaction._2
        (
          balances + (name -> (balances(name) - amount)),
          transaction
        )
      }
    }

  test("Using State monad, we get the final balance calculation, while also maintaining transaction history.") {
    val result = updateBalancesStateMonad(transactions).run(initialBalances).value
    result._1 shouldBe Map("Alice" -> 0, "Bob" -> 100)
    result._2 shouldBe transactions
  }

  def updateBalancesStream[F[_]](
    initialBalances: Balances,
    transactions: Stream[F, Transaction]
  ): Stream[F, (Balances, Transaction)] =
    transactions.mapAccumulate(initialBalances) { case (balances, (name, amount)) =>
      val newBalance = balances + (name -> (balances(name) - amount))
      (newBalance, (name, amount))
    }

  test("Using fs2.Stream.") {
    val result = updateBalancesStream(initialBalances, Stream.emits(transactions)).compile.toVector
    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), ("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), ("Bob", 50))
    )
  }

  test("Using fs2.Stream, lifted to IO.") {
    val result = updateBalancesStream[IO](initialBalances, Stream.emits(transactions)).compile.toVector.unsafeRunSync
    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), ("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), ("Bob", 50))
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
      ("Alice", BigDecimal(50)),
      ("Alice", BigDecimal(50)),
      ("Bob", BigDecimal(50)),
      ("Bob", BigDecimal(50))
    )
    val transactionsB = Vector(
      ("Mary", BigDecimal(50)),
      ("Mary", BigDecimal(50)),
      ("John", BigDecimal(50)),
      ("John", BigDecimal(50))
    )
    val transactionsC = Vector(
      ("Anthony", BigDecimal(50)),
      ("Anthony", BigDecimal(50)),
      ("Sarah", BigDecimal(50)),
      ("Sarah", BigDecimal(50))
    )

    val streamA = Stream.emits(transactionsA)
    val streamB = Stream.emits(transactionsB)
    val streamC = Stream.emits(transactionsC)

    val combinedStream = Stream(streamA, streamB, streamC).flatten

    val result = combinedStream.compile.toVector
    println(result)
  }
}
