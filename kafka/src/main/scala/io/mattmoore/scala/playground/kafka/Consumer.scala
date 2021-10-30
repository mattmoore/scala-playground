package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object Consumer {
  def connection[F[_]: Async](broker: String): Resource[F, KafkaConsumer[F, String, String]] = {
    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(broker)
        .withGroupId("group")

    KafkaConsumer
      .resource(consumerSettings)
  }
}
