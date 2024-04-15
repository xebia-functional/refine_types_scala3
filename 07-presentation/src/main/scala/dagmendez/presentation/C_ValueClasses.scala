package dagmendez.presentation

object C_ValueClasses:

  class DniNumber(val number: String):
    override def toString: String = number
  end DniNumber

  class DniLetter(val letter: String):
    override def toString: String = letter
  end DniLetter

  class DNI(number: DniNumber, letter: DniLetter):
    // Additional nesting to access the wrapped values
    require(letter.letter == controlDigit(number.number.toInt % 23))
    override def toString: String = s"$number-$letter"
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      // requirement failed: Verify the DNI. Control letter does not match the number.
      DNI(DniNumber("00000001"), DniLetter("A")),
      // java.lang.NumberFormatException: For input string: "R"
      DNI(DniNumber("R"), DniLetter("00000001")),
      // Valid DNI
      DNI(DniNumber("00000001"), DniLetter("R"))
    ).map(_.toString).foreach(println)

/**
 * Conclusion: Value Classes give us some enforcement of order but not much more.
 */
