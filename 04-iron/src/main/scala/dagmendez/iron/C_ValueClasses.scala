package dagmendez.iron

object C_ValueClasses:

  class Number(val value: Int)

  class Letter(val value: String)

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): DNI =
      new DNI(Number(number), Letter(letter))

  val valid                   = DNI(1, "R")
  val negativeNumber          = DNI(-1, "R")
  val tooLongNumber           = DNI(1234567890, "R")
  val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid)
    println("== Invalid DNIs ==")
    println(negativeNumber)
    println(tooLongNumber)
    println(invalidControlLetterDNI)

/**
 * Conclusion: Value Classes give us some enforcement of order but not much more.
 */
