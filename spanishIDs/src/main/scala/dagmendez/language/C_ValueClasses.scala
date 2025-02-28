package dagmendez.language

object C_ValueClasses:

  class NieLetter(val value: String)
  class Number(val value: Int)
  class Letter(val value: String)

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): DNI =
      new DNI(Number(number), Letter(letter))

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object NIE:
    def apply(nieLetter: String, number: Int, letter: String): NIE =
      new NIE(NieLetter(nieLetter), Number(number), Letter(letter))

  val validDNI = DNI(1, "R")
  val validNIE = NIE("X", 1, "R")

  val invalidNIELetter        = NIE("A", 1, "R")
  val invalidNegativeNumber   = DNI(-1, "R")
  val invalidTooLongNumber    = DNI(1234567890, "R")
  val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)

/**
 * Conclusion: Value Classes give us some enforcement of order but not much more.
 */
