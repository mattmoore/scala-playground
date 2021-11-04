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

  // Using fold, we get the final balance result, but do not keep the history of transactions in the output.
  test("Running totals using fold with tuples") {
    def updateBalances(balances: Balances, transactions: Vector[Transaction]): Map[String, BigDecimal] =
      transactions.foldLeft(balances) { case (bal, (name, amount)) =>
        bal + (name -> (bal(name) - amount))
      }

    val result = updateBalances(initialBalances, transactions)

    result shouldBe Map("Alice" -> 0, "Bob" -> 100)
  }

  // Using State monad, we get the final balance calculation, while also maintaining transaction history.
  test("Running totals using State monad") {
    def updateBalances(transactions: Vector[Transaction]): State[Balances, Vector[Transaction]] =
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

    val result = updateBalances(transactions).run(initialBalances).value

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

  // Using an fs2 Stream we no longer have to load the entire list into memory.
  test("Running totals with fs2 Stream (Pure)") {
    val result = updateBalancesStream(initialBalances, Stream.emits(transactions)).compile.toVector

    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), ("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), ("Bob", 50))
    )
  }

  // Also using fs2 Stream, but we're lifting to IO instead of Pure.
  test("Running totals with fs2 Stream (IO)") {
    val result = updateBalancesStream[IO](initialBalances, Stream.emits(transactions)).compile.toVector.unsafeRunSync

    result shouldBe Vector(
      (Map("Alice" -> 50, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 200), ("Alice", 50)),
      (Map("Alice" -> 0, "Bob" -> 150), ("Bob", 50)),
      (Map("Alice" -> 0, "Bob" -> 100), ("Bob", 50))
    )
  }
}
