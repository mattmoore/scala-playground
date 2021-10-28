package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object Producer extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val key = args(0)
    val value = args.drop(1).mkString(" ")

    val producerSettings =
      ProducerSettings[IO, String, String]
        .withBootstrapServers("localhost:9092")

    KafkaProducer
      .stream(producerSettings)
      .evalMap { producer =>
        producer.produce(
          ProducerRecords.one(
            ProducerRecord("topic", key, value)
          )
        )
      }
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
