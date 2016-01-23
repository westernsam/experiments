
object JsonSerialization {
  def toJson(m: Map[String, Any]): String = {

    def jsonify(any: Any): String = any match {
      case map: Map[_, Any] =>
        map.map {
          case (key: String, value) => s""""$key":${jsonify(value)}"""
          case t => throw new IllegalArgumentException(s"Can't jsonify maps with keys that are not Strings. Key ${t._1} is a ${t._1.getClass.getName}")
        }.mkString("{", ",", "}")

      case list: Traversable[Any] =>
        list.map(jsonify).mkString("[", ",", "]")

      //arrays are not traversable
      case array: Array[Any] =>
        array.map(jsonify).mkString("[", ",", "]")

      case str: String => s""""$str""""
      case boolean: Boolean => s"$boolean"
      case number: Number => s"${number.toString}"

      case nil if nil == null => s"null"
      case _ => throw new IllegalArgumentException(s"Can't jsonify $any of type ${any.getClass.getName}")
    }

    jsonify(m)
  }

}