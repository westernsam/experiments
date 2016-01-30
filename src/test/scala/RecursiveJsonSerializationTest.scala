import JsonSerialization.{RecursiveJsonSerializer, JsonSerilizer}
import org.scalatest.FunSpec


class RecursiveJsonSerializationTest extends FunSpec with JsonSerializationTest {
  override def jsonSerializer: JsonSerilizer = new RecursiveJsonSerializer
}
