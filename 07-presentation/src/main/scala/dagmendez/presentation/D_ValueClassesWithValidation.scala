package dagmendez.presentation

object D_ValueClassesWithValidation:

  class DniNumber(val number: String):
    require(
      number.length == 8,
      "There must be 8 numbers."
    )
    require(
      number.forall(_.isDigit),
      "The leading 8 characters must be digits."
    )
    override def toString: String = number
  end DniNumber

  class DniLetter(val letter: String):
    require(
      controlDigit.values.toVector.contains(letter),
      "Invalid control letter."
    )
    override def toString: String = letter
  end DniLetter

  class DNI(number: DniNumber, letter: DniLetter):
    require(
      letter.letter == controlDigit(number.number.toInt % 23),
      "Verify the DNI. Control letter does not match the number."
    )

    override def toString: String = s"$number-$letter"
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      // requirement failed: Verify the DNI. Control letter does not match the number.
      DNI(DniNumber("00000001"), DniLetter("A")),
      // requirement failed: There must be 8 numbers.
      DNI(DniNumber("R"), DniLetter("00000001")),
      // requirement failed: The leading 8 characters must be digits.
      DNI(DniNumber("123BCD78"), DniLetter("A")),
      // requirement failed: Invalid control letter.
      DNI(DniNumber("12345678"), DniLetter("U")),
      // requirement failed: Verify the DNI. Control letter does not match the number.
      DNI(DniNumber("12345678"), DniLetter("B")),
      // Valid DNIs
      DNI(DniNumber("00000001"), DniLetter("R")),
      DNI(DniNumber("12345678"), DniLetter("Z"))
    ).foreach(println)

/**
 * Conclusion: Value Classes give us order enforcement and specialized errors.
 */
