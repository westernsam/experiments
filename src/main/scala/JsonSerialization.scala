import java.io._

object JsonSerialization {

  def toJson(m: Map[String, Any]): String = {
    val w: StringWriter = new StringWriter()
    toJson(m, w)
    w.toString
  }

  def toJson(m: Map[String, Any], outputStream: OutputStream): Unit = toJson(m, new OutputStreamWriter(outputStream))

  /*
  *
  * Large strings can cause GC pressure - because they require contiguous char[].
  * If you are only creating some JSON to write it somewhere else then it make sense not to store it in a string first.
  * This version is not tail recursive: so would cause a StackOverflow for a deeply recursive structure (normally the 1000th map within map)
  * */
  def toJson(m: Map[String, Any], w: Writer): Unit = {
    def writeAny(any: Any): Unit = any match {
      case map: Map[_, Any] =>
        w.append('{')
        for {((key, value), index) <- map.zipWithIndex} {
          if (!key.isInstanceOf[String])
            throw new IllegalArgumentException(s"Can't jsonify maps with keys that are not Strings. Key $key is a ${key.getClass.getName}")

          w.append(s""""$key":""")
          writeAny(value)
          if (index + 1 < map.size)
            w.write(",")
        }
        w.append('}')

      case it: Iterable[Any] =>
        w.append('[')
        for {(elem, index) <- it.zipWithIndex} {
          writeAny(elem)
          if (index + 1 < it.size)
            w.write(",")
        }
        w.append(']')

      //arrays are not traversable
      case array: Array[Any] =>
        w.append('[')
        for {(elem, index) <- array.zipWithIndex} {
          writeAny(elem)
          if (index + 1 < array.length)
            w.write(",")
        }
        w.append(']')

      case str: String => w.write(s""""$str"""")
      case boolean: Boolean => w.write(s"$boolean")
      case number: Number => w.write(s"${number.toString}")
      case nil if nil == null => w.write("null")

      case _ => throw new IllegalArgumentException(s"Can't jsonify $any of type ${any.getClass.getName}")
    }

    writeAny(m)
  }

}