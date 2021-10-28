package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object Consumer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    KafkaConsumer
      .stream(consumerSettings)
      .subscribeTo("topic")
      .records
      .evalMap { committable =>
        IO(println(s"Processing record: ${committable.record}"))
      }
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
