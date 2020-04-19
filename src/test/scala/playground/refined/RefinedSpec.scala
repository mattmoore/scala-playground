package playground.refined

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import playground.Hello

class RefinedSpec extends AnyFlatSpec with Matchers {
  "The Hello object" should "say hello" in {
    Hello.greeting shouldEqual "hello"
  }
}
