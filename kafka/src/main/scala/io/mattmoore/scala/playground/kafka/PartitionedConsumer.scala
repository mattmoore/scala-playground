package io.mattmoore.scala.playground.kafka

import cats.effect._
import fs2.kafka._

object PartitionedConsumer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId("group")

    KafkaConsumer
      .stream(consumerSettings)
      .subscribeTo("topic")
      .partitionedRecords
      .map { partitionStream =>
        partitionStream.evalMap { committable =>
          IO(println(s"Processing record: ${committable.record}"))
        }
      }
      .parJoinUnbounded
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
