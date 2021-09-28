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
    (g.homeScore > g.awayScore && g.winner.contains(g.homeTeam)) ||
    (g.awayScore > g.homeScore && g.winner.contains(g.awayTeam)) ||
    (g.winner.isEmpty)
  }
}
