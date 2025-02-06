package dagmendez.language

object A_RawClasses:

  class DNI(number: Int, letter: String):
    override def toString: String = s"$number-$letter"

  class NIE(nieLetter: String, number: Int, letter: String):
    override def toString: String = s"$nieLetter-$number-$letter"

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
