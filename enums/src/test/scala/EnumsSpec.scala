import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class EnumsSpec extends AnyFunSpec with Matchers {
  describe("using case objects") {
    sealed trait NewOrRepeatDine { def toString: String }
    object NewOrRepeatDine {
      case object New extends NewOrRepeatDine { override def toString = "N" }
      case object Repeat extends NewOrRepeatDine { override def toString = "R" }
      case object NotApplicable extends NewOrRepeatDine { override def toString = "" }

      def fromString(v: String): Option[NewOrRepeatDine] = v match {
        case "N" => Option(New)
        case "R" => Option(Repeat)
        case ""  => Option(NotApplicable)
        case _   => None
      }
    }
    import NewOrRepeatDine._

    it("transforms to string") {
      s"${New}" shouldBe "N"
      s"${Repeat}" shouldBe "R"
      s"${NotApplicable}" shouldBe ""
    }

    it("parses from string") {
      NewOrRepeatDine.fromString("N") shouldBe Some(New)
      NewOrRepeatDine.fromString("R") shouldBe Some(Repeat)
      NewOrRepeatDine.fromString("") shouldBe Some(NotApplicable)
      NewOrRepeatDine.fromString(" ") shouldBe None
    }
  }
}
