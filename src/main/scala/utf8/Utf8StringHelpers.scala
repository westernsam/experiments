package utf8

import java.nio.charset.StandardCharsets

/**
  * Created by sam on 12/05/17.
  */
object Utf8StringHelpers {

  private val utf8 = StandardCharsets.UTF_8

  implicit class RichUtf8String(str: String) {
    def utf8: Utf8String = new Utf8String(str.getBytes(Utf8StringHelpers.utf8.toString))
  }

}
