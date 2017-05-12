package json

import json.JsonSerialization.{JsonSerilizer, NonRecursiveJsonSerializer}
import org.scalatest.FunSpec

class NonRecursiveJsonSerializationTest extends FunSpec with JsonSerializationTest {
  override def jsonSerializer: JsonSerilizer = new NonRecursiveJsonSerializer
}
