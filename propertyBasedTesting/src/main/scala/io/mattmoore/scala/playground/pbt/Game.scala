package io.mattmoore.scala.playground.pbt

case class Game(homeTeam: Team, awayTeam: Team, homeScore: Int, awayScore: Int) {
  val winner: Option[Team] = {
    if (homeScore > awayScore) Some(homeTeam)
    else if (awayScore > homeScore) Some(awayTeam)
    else None
  }
}
