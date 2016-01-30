import JsonSerialization.{JsonSerilizer, NonRecursiveJsonSerializer}
import org.scalatest.FunSpec
import org.scalatest.Matchers._

trait JsonSerializationTest {
  self: FunSpec =>

  def jsonSerializer: JsonSerilizer

  describe("convert map to json") {

    it("writes empty json") {
      jsonSerializer.toJson(Map.empty) should be("{}")
    }

    it("writes json string") {
      val json: String = jsonSerializer.toJson(Map("link" -> "link++"))
      println(json)
      json should be("""{"link":"link++"}""")
    }

    it("writes list string") {
      jsonSerializer.toJson(Map("link" -> Seq("link++"))) should be("""{"link":["link++"]}""")
      //      toJson(Map("link" -> Array("link++"))) should be("""{"link":["link++"]}""")
      jsonSerializer.toJson(Map("link" -> List("link++"))) should be("""{"link":["link++"]}""")
      jsonSerializer.toJson(Map("link" -> Seq("link++").toIterable)) should be("""{"link":["link++"]}""")
    }

    it("writes numbers") {
      jsonSerializer.toJson(
        Map(
          "long" -> 10L,
          "int" -> 1,
          "float" -> 0.1F,
          "double" -> 0.01D
        )
      ) should be("""{"long":10,"int":1,"float":0.1,"double":0.01}""")
    }

    it("writes booleans") {
      jsonSerializer.toJson(Map(
        "true" -> true,
        "false" -> false)
      ) should be("""{"true":true,"false":false}""")
    }

    it("writes nulls") {
      jsonSerializer.toJson(Map("null" -> null)
      ) should be("""{"null":null}""")
    }

    it("throws exception when given non-supported type") {
      intercept[IllegalArgumentException] {
        jsonSerializer.toJson(Map("hi" -> new Object))
      }
    }

    it("throws exception when contains map with non-string key") {
      intercept[IllegalArgumentException] {
        jsonSerializer.toJson(Map("hi" -> Map(1 -> 1)))
      }
    }
  }



}