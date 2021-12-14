package services

import com.dimafeng.testcontainers._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MySQLTimeSpec extends AnyFreeSpec with Matchers with ForAllTestContainer {
  override def container: Container = MySQLContainer()

  "MySQL has no real timezone suppport" - {
    "MySQL doesn't handle timezones correctly" in {}
  }
}
