package io.mattmoore.scala.playground.catamorphisms

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RunningState extends AnyFunSuite with Matchers {
  test("Running totals using fold with tuples") {

    def calculateBalances(
      balances: Map[String, BigDecimal],
      transactions: List[(String, BigDecimal)]
    ): Map[String, BigDecimal] =
      transactions.foldLeft(balances) { case (bal, (name, amount)) =>
        bal + (name -> (bal(name) - amount))
      }

    val remainingBalances = calculateBalances(
      balances = Map(
        "Alice" -> BigDecimal(100),
        "Bob" -> BigDecimal(200)
      ),
      transactions = List(
        ("Alice", BigDecimal(50)),
        ("Alice", BigDecimal(50)),
        ("Bob", BigDecimal(50)),
        ("Bob", BigDecimal(50))
      )
    )

    remainingBalances shouldBe Map(
      "Alice" -> 0,
      "Bob" -> 100
    )
  }
}
