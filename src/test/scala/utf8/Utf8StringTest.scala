package utf8

import org.scalatest.FunSpec
import utf8.Utf8StringHelpers._
import org.scalatest.Matchers._

class Utf8StringTest extends FunSpec {

  describe("has the right length for") {
    it("1 byte chars") {
      "\u0024".utf8 should have (length (1))
    }

    it("2 byte chars") {
      "\u00A2".utf8 should have (length (1))
    }

    it("3 byte chars") {
      "\u20AC".utf8 should have (length (1))
    }

    it("4 byte chars") {
      "\uD83D\uDCA9".utf8 should have (length (1))
    }

    it("a mix") {
      "\u0024 \u00A2 \u20AC \uD83D\uDCA9".utf8 should have (length (7))
    }
  }

  describe("gets the right code point for") {
    ignore("1 byte chars") {
      "\u0024".utf8.codePointAt(0) shouldBe -1
    }

    ignore("2 byte chars") {
      "\u00A2".utf8.codePointAt(0) shouldBe -1
    }

    ignore("3 byte chars") {
      "\u20AC".utf8.codePointAt(0) shouldBe -1
    }

    ignore("4 byte chars") {
      "\uD83D\uDCA9".utf8.codePointAt(0) shouldBe -1
    }
  }

}
