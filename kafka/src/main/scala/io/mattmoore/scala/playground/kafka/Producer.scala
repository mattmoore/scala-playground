package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object Producer {
  def connection[F[_]: Async](broker: String): Resource[F, KafkaProducer[F, String, String]] = {
    val producerSettings =
      ProducerSettings[F, String, String]
        .withBootstrapServers(broker)

    KafkaProducer
      .resource(producerSettings)
  }
}
