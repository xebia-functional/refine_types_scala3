package dagmendez.presentation

object C_ValueClasses:

  class DniNumber(val number: String):
    override def toString: String = number
  end DniNumber

  class DniLetter(val letter: String):
    override def toString: String = letter
  end DniLetter

  class DNI(number: DniNumber, letter: DniLetter):
    override def toString: String = s"$number-$letter"


  def main(args: Array[String]): Unit =
    Vector(
      // Valid DNI
      DNI(DniNumber("00000001"), DniLetter("R")),
      // Invalid DNIs
      DNI(DniNumber("00000001"), DniLetter("A")),
      DNI(DniNumber("R"), DniLetter("00000001"))
    ).map(_.toString).foreach(println)

/**
 * Conclusion: Value Classes give us some enforcement of order but not much more.
 */
