import java.io._

import scala.annotation.tailrec
import scala.collection.mutable

object JsonSerialization {

  def toJson(m: Map[String, Any]): String = {
    val w: StringWriter = new StringWriter()
    toJson_noTailRecurse(m, w)
    w.toString
  }

  def toJson(m: Map[String, Any], outputStream: OutputStream): Unit = toJson(m, new OutputStreamWriter(outputStream))


  /*
  * A version that is tail recursive. Safe to use for deeply recursive maps
  * */
  def toJson_noTailRecurse(m: Map[String, Any], w: Writer): Unit = {

    sealed trait Event
    sealed case class StartArray() extends Event
    sealed case class StartMap() extends Event
    sealed case class Comma() extends Event

    @tailrec
    def writeAny(stack: mutable.Stack[Any]): Unit = {
      if (stack.isEmpty) return

      stack.pop() match {
        case _: StartMap => w.write('{')
        case _: StartArray => w.write('[')
        case _: Comma => w.write(',')

        case map: Map[_, Any] =>

          map.headOption match {
            case Some(keyValue) =>
              if (!keyValue._1.isInstanceOf[String])
                throw new IllegalArgumentException(s"Can't jsonify maps with keys that are not Strings. Key $keyValue._1 is a ${keyValue._1.getClass.getName}")

              w.write(s""""${keyValue._1}":""")
              stack.push(map.tail)
              if (map.size > 1)
                stack.push(Comma())
              stack.push(keyValue._2)
              keyValue._2 match {
                case _: Map[_, Any] => stack.push(StartMap())
                case _: Iterable[Any] => stack.push(StartArray())
                case _ =>
              }

            case None =>
              w.write('}')
          }

        case list: Iterable[Any] =>
          list.headOption match {
            case Some(value) =>
              stack.push(list.tail)
              if (list.size > 1)
                stack.push(Comma())
              stack.push(value)
              value match {
                case _: Map[_, Any] => stack.push(StartMap())
                case _: Iterable[Any] => stack.push(StartArray())
                case _ =>
              }

            case None =>
              w.write(']')
          }

        case str: String => w.write(s""""$str"""")
        case boolean: Boolean => w.write(s"$boolean")
        case number: Number => w.write(s"${number.toString}")
        case nil if nil == null => w.write("null")

        case any => throw new IllegalArgumentException(s"Can't jsonify $any of type ${any.getClass.getName}")
      }

      writeAny(stack)
    }

    val stack: mutable.Stack[Any] = new mutable.Stack[Any]()
    stack.push(m)
    stack.push(StartMap())
    writeAny(stack)
  }

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