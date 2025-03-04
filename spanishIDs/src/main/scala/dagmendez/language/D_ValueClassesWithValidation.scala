package dagmendez.language

import dagmendez.common.controlLetter

object D_ValueClassesWithValidation:

  class NieLetter(val value: String):
    require(Set("X", "Y", "Z").contains(value.toUpperCase), "Valid NIE letters are X, Y, Z.")

  class Number(val value: Int):
    require(value > 0, "Number has to be positive.")
    require(value <= 99999999, "Maximum amount of numbers is 8.")

  class Letter(val value: String):
    require(controlLetter.values.toSeq.contains(value), "Invalid control letter.")

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

  // We include lazy so the values are not initialized till they are called in the println.
  lazy val validDNI = DNI(1, "R")
  lazy val validNIE = NIE("X", 1, "R")

  lazy val invalidNIELetter        = NIE("A", 1, "R")
  lazy val invalidNegativeNumber   = DNI(-1, "R")
  lazy val invalidTooLongNumber    = DNI(1234567890, "R")
  lazy val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =

    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    // These won't be printed. Instead, there is going to be a StackTrace error.
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)

/**
 * Conclusion: Value Classes give us order enforcement and specialized errors.
 */
