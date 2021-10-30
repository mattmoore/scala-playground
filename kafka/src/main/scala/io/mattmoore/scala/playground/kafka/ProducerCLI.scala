package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object ProducerCLI extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val (key, value) = (args.head, args.drop(1).mkString(" "))

    val producerSettings = ProducerSettings[IO, String, String]
      .withBootstrapServers("localhost:9092")

    KafkaProducer
      .stream(producerSettings)
      .evalMap(_.produce(ProducerRecords.one(ProducerRecord("topic", key, value))))
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
