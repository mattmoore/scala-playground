package io.mattmoore.scala.playground.kafka

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import fs2.kafka._
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.DurationInt

class KafkaSuite extends AsyncFunSuite with AsyncIOSpec with ForAllTestContainer with Matchers {
  override val container: KafkaContainer = KafkaContainer()

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  test("Producer and consumer example") {
    val events = fs2.Stream.emits(List(("8f68ba9c-241e-44c3-b552-841612f5ec4e", "This is the first message.")))

    val producerSettings = ProducerSettings[IO, String, String]
      .withBootstrapServers(container.bootstrapServers)
      .withAcks(Acks.All)
    val consumerSettings = ConsumerSettings[IO, String, String]
      .withBootstrapServers(container.bootstrapServers)
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withGroupId("group")
    val topic = "topic"

    val result = for {
      producer <- KafkaProducer.stream(producerSettings)
      _ <- events
        .map(m => ProducerRecord(topic, m._1, m._2))
        .map(m => ProducerRecords.one(m))
        .evalMap(producer.produce)
      consumer <- KafkaConsumer
        .stream(consumerSettings)
        .evalTap(_.subscribeTo(topic))
      messages <- consumer.records
    } yield messages

    result.take(1).timeout(10.seconds).compile.lastOrError.asserting { message =>
      (message.record.key, message.record.value) shouldBe
        ("8f68ba9c-241e-44c3-b552-841612f5ec4e", "This is the first message.")
    }
  }
}
