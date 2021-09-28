package io.mattmoore.scala.playground.pbt

import org.scalacheck.Prop._
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck._

object GameShapelessSpec extends Properties("GameShapeless") {
  property("winner") = forAll { g: Game =>
    (g.homeScore > g.awayScore && g.winner.contains(g.homeTeam)) ||
    (g.awayScore > g.homeScore && g.winner.contains(g.awayTeam)) ||
    (g.winner.isEmpty)
  }
}
