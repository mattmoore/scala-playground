package io.mattmoore.scala.playground.kafka

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import fs2.kafka.{ProducerRecord, ProducerRecords}
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class KafkaSuite extends AsyncFunSuite with AsyncIOSpec with ForAllTestContainer with Matchers {
  override val container: KafkaContainer = KafkaContainer()

  val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  test("Producer and consumer example") {
    val events = fs2.Stream.emits(
      List(
        ("8f68ba9c-241e-44c3-b552-841612f5ec4e", "This is the first message."),
        ("fd79c027-a6fa-4f00-86d4-3a9ec90115e6", "A second message."),
        ("e47b17fb-72c9-4cf2-b6db-226f259b6c6a", "And yet a third message.")
      )
    )

    val result =
      for {
        producer <- fs2.Stream.resource(Producer.connection[IO](container.bootstrapServers))
        _ <- fs2.Stream.eval(
          producer.produce(ProducerRecords.one(ProducerRecord("topic", "1", "Test message")))
        )
        consumer <- fs2.Stream.resource(
          Consumer
            .connection[IO](container.bootstrapServers)
            .evalTap(_.subscribeTo("topic"))
        )
        message <- consumer.records.take(1)
      } yield message

    result.compile.lastOrError.asserting { c =>
      (c.record.key, c.record.value) shouldBe ("1", "Test message")
    }
  }
}
