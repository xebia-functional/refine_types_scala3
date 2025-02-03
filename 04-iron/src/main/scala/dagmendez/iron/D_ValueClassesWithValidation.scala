package dagmendez.iron

object D_ValueClassesWithValidation:

  class Number(val value: Int):
    require(value > 0, "Number has to be positive.")
    require(value <= 99999999, "Maximum amount of numbers is 8.")

  class Letter(val value: String):
    require(controlDigit.values.exists(_controlDigit => value == _controlDigit), "Invalid control letter.")

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): DNI =
      new DNI(Number(number), Letter(letter))

  // We include lazy so the values are not initialized till they are called in the println.
  lazy val valid                   = DNI(1, "R")
  lazy val negativeNumber          = DNI(-1, "R")
  lazy val tooLongNumber           = DNI(1234567890, "R")
  lazy val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid)

    println("== Invalid DNIs ==")
    // These won't be printed. Instead, there is going to be a StackTrace error.
    println(negativeNumber)
    println(tooLongNumber)
    println(invalidControlLetterDNI)

/**
 * Conclusion: Value Classes give us order enforcement and specialized errors.
 */
