package io.mattmoore.scala.playground.kafka

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import fs2.kafka._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.DurationInt

class KafkaSuite extends AnyFunSuite with ForAllTestContainer with Matchers {
  override val container: KafkaContainer = KafkaContainer()

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  test("Producer and consumer example") {
    val messages = List(
      ("8f68ba9c-241e-44c3-b552-841612f5ec4e", "This is the first message."),
      ("fd79c027-a6fa-4f00-86d4-3a9ec90115e6", "A second message."),
      ("e47b17fb-72c9-4cf2-b6db-226f259b6c6a", "And yet a third message.")
    )

    val events = fs2.Stream.emits(messages)

    val producerSettings = ProducerSettings[IO, String, String]
      .withBootstrapServers(container.bootstrapServers)
      .withAcks(Acks.All)
    val consumerSettings = ConsumerSettings[IO, String, String]
      .withBootstrapServers(container.bootstrapServers)
      .withGroupId("group")
      .withAutoOffsetReset(AutoOffsetReset.Earliest)

    val topic = "topic"

    val results = for {
      _ <- KafkaProducer.stream(producerSettings).flatMap { producer =>
          events
            .map(m => ProducerRecord(topic, m._1, m._2))
            .map(m => ProducerRecords.one(m))
            .evalMap(producer.produce)
        }.compile.drain
      messages <- KafkaConsumer.stream(consumerSettings).subscribeTo(topic)
        .records.take(3).compile.toList
    } yield messages

    results.timeout(10.seconds).unsafeRunSync()
      .map(it => (it.record.key, it.record.value)) shouldBe messages
  }
}
