package services

import cats.effect._
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers._
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import db.TransactionRepository
import domain.Transaction
import doobie._
import org.flywaydb.core.Flyway
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.time._

class PostgresTimeSpec extends AnyFreeSpec with Matchers with TestContainersForAll {
  override type Containers = PostgreSQLContainer

  override def startContainers(): PostgreSQLContainer = {
    val psql = PostgreSQLContainer.Def("postgres:14").start()
    Flyway
      .configure()
      .mixed(true)
      .baselineOnMigrate(true)
      .dataSource(psql.container.getJdbcUrl, psql.container.getUsername, psql.container.getPassword)
      .load()
      .migrate()
    psql
  }

  "PostgreSQL time handling" - {
    "PostgreSQL has better timezone support" in {
      withContainers { psql =>
        val transactor = Transactor.fromDriverManager[IO](
          driver = psql.driverClassName,
          url = psql.jdbcUrl,
          user = psql.username,
          pass = psql.password
        )
        val transactionRepo = new TransactionRepository[IO](transactor)
        val transactionService = new TransactionService[IO](transactionRepo)

        val transactionToInsert = Transaction(
          date = ZonedDateTime.of(
            LocalDate.of(2021, 11, 1),
            LocalTime.of(0, 0, 0),
            ZoneId.of("UTC")
          )
        )
        val id = transactionService.add(transactionToInsert).unsafeRunSync()
        val expected = ZonedDateTime.of(
          LocalDate.of(2021, 11, 1),
          LocalTime.of(0, 0, 0),
          ZoneId.of("UTC")
        )
        val actual = transactionService.get(id).unsafeRunSync().date
        actual shouldBe expected
      }
    }
  }
}
