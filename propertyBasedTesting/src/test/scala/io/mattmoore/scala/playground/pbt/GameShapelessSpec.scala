package io.mattmoore.scala.playground.pbt

import io.mattmoore.scala.playground.pbt.TeamShapelessSpec.genTeam
import org.scalacheck.Prop._
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck._

object GameShapelessSpec extends Properties("GameShapeless") {
  val genGame: Gen[Game] = for {
    homeTeam <- genTeam
    awayTeam <- genTeam
    homeScore <- Gen.choose(0, 100)
    awayScore <- Gen.choose(0, 100)
  } yield Game(homeTeam, awayTeam, homeScore, awayScore)

  property("winner") = forAll(genGame) { g: Game =>
    s"g.homeScore: ${g.homeScore}, g.awayScore: ${g.awayScore}, g.winner: ${g.winner}" |:
      g.homeScore > g.awayScore ==> (g.winner.contains(g.homeTeam))

    s"g.awayScore: ${g.awayScore}, g.homeScore: ${g.homeScore}, g.winner: ${g.winner}" |:
      g.awayScore > g.homeScore ==> (g.winner.contains(g.awayTeam))
  }
}
