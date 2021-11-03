package io.mattmoore.scala.playground.catamorphisms

import cats.data._
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RunningState extends AnyFunSuite with Matchers {
  type Balances = Map[String, BigDecimal]
  type Transaction = (String, BigDecimal)

  // Using fold, we get the final balance result, but do not keep the history of transactions in the output.
  test("Running totals using fold with tuples") {
    def updateBalances(balances: Balances, transactions: List[Transaction]): Map[String, BigDecimal] =
      transactions.foldLeft(balances) { case (bal, (name, amount)) =>
        bal + (name -> (bal(name) - amount))
      }

    val initialBalances = Map(
      "Alice" -> BigDecimal(100),
      "Bob" -> BigDecimal(200)
    )

    val transactions = List(
      ("Alice", BigDecimal(50)),
      ("Alice", BigDecimal(50)),
      ("Bob", BigDecimal(50)),
      ("Bob", BigDecimal(50))
    )

    val result = updateBalances(initialBalances, transactions)

    result shouldBe Map("Alice" -> 0, "Bob" -> 100)
  }

  // Using State monad, we get the final balance calculation, while also maintaining transaction history.
  test("Running totals using State monad") {
    def updateBalances(transactions: List[Transaction]): State[Balances, List[Transaction]] =
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

    val initialBalances = Map(
      "Alice" -> BigDecimal(100),
      "Bob" -> BigDecimal(200)
    )

    val transactions = List(
      ("Alice", BigDecimal(50)),
      ("Alice", BigDecimal(50)),
      ("Bob", BigDecimal(50)),
      ("Bob", BigDecimal(50))
    )

    val result = updateBalances(transactions).run(initialBalances).value

    result._1 shouldBe Map("Alice" -> 0, "Bob" -> 100)
    result._2 shouldBe transactions
  }
}
