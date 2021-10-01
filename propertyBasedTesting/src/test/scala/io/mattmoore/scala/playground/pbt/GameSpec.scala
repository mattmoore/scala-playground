package io.mattmoore.scala.playground.pbt

import TeamSpec.genTeam
import org.scalacheck.Prop._
import org.scalacheck._

object GameSpec extends Properties("Game") {
  val genGame: Gen[Game] = for {
    homeTeam <- genTeam
    awayTeam <- genTeam
    homeScore <- Gen.choose(0, 100)
    awayScore <- Gen.choose(0, 100)
  } yield Game(homeTeam, awayTeam, homeScore, awayScore)

  implicit lazy val arbitraryGame: Arbitrary[Game] = Arbitrary(genGame)

  property("winner") = forAll { g: Game =>
    s"g.homeScore: ${g.homeScore}, g.awayScore: ${g.awayScore}, g.winner: ${g.winner}" |:
      g.homeScore > g.awayScore ==> (g.winner.contains(g.homeTeam))

    s"g.awayScore: ${g.awayScore}, g.homeScore: ${g.homeScore}, g.winner: ${g.winner}" |:
      g.awayScore > g.homeScore ==> (g.winner.contains(g.awayTeam))
  }
}
