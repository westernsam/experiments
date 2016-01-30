import JsonSerialization.{NonRecursiveJsonSerializer, JsonSerilizer}
import org.scalatest.FunSpec

class NonRecursiveJsonSerializationTest extends FunSpec with JsonSerializationTest {
  override def jsonSerializer: JsonSerilizer = new NonRecursiveJsonSerializer
}
