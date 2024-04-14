package dagmendez.presentation

object B_TypeAliases:

  type DniNumber      = String
  type DniControlChar = String

  class DNI private (number: DniNumber, letter: DniControlChar):
    require(letter == controlDigit(number.toInt % 23))
    override def toString: String = s"$number-$letter"
  end DNI

  object DNI:
    def fromString(dni: String): DNI =
      val dniValues: Array[DniNumber] = dni.split("-")
      val number: DniNumber           = dniValues.head
      val letter: DniControlChar      = dniValues(1)
      DNI(number, letter)
  end DNI

  def main(args: Array[DniNumber]): Unit =
    Vector(
      DNI.fromString("R-0000001"), // Will cause program to crash
      DNI.fromString("00000001-R")
    ).foreach(println)

  /**
   * Conclusion: Type Aliases increases code readability but do not give as any additional guaranties
   */
