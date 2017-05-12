package json

import json.JsonSerialization.{JsonSerilizer, RecursiveJsonSerializer}
import org.scalatest.FunSpec


class RecursiveJsonSerializationTest extends FunSpec with JsonSerializationTest {
  override def jsonSerializer: JsonSerilizer = new RecursiveJsonSerializer
}
