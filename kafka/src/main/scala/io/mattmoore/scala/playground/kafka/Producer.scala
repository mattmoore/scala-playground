package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object Producer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val producerSettings =
      ProducerSettings[IO, String, String]
        .withBootstrapServers("localhost:9092")

    val record = ProducerRecord("topic", args(1), args.drop(1).mkString(" "))

    KafkaProducer
      .stream(producerSettings)
      .evalMap { producer =>
        producer.produce(ProducerRecords.one(record))
      }
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
