package dagmendez.iron

object D_ValueClassesWithValidation:

  class Number(val value: Int):
    require(
      value > 0,
      "Number has to be positive."
    )
    require(
      value <= 99999999,
      "Maximum amount of numbers is 8."
    )
  end Number

  class Letter(val value: String):
    require(
      controlDigit.values.exists(_controlDigit => value == _controlDigit),
      "Invalid control letter."
    )
  end Letter

  class DNI(number: Number, letter: Letter):
    override def toString: String = prettyDNI(number.value, letter.value)

  def main(args: Array[String]): Unit =

    println("== Valid DNIs ==")
    println(DNI(Number(1), Letter("R")))

    // Remove comments for any Invalid DNI to make the program crush during runtime

    // == Invalid DNIs ==
    // Negative Number:
    // println(DNI(Number(-1), Letter("R")))
    // Too long number:
    // println(DNI(Number(1234567890), Letter("R")))
    // Incorrect control letter:
    // println(DNI(Number(1), Letter("Ñ")))

/**
 * Conclusion: Value Classes give us order enforcement and specialized errors.
 */
