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
    override def toString: String = s"$number-$letter"
  
  def main(args: Array[String]): Unit =
    Vector(
      // Valid DNIs
      DNI(DniNumber("00000001"), DniLetter("R")),
      DNI(DniNumber("12345678"), DniLetter("Z")),
      // Invalid DNIs - Execution will crash
      DNI(DniNumber("R"), DniLetter("00000001")),
      DNI(DniNumber("123BCD78"), DniLetter("A")),
      DNI(DniNumber("12345678"), DniLetter("U")),
    ).foreach(println)

/**
 * Conclusion: Value Classes give us order enforcement and specialized errors.
 */
