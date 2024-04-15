package dagmendez.presentation

object B_TypeAliases:

  type DniNumber      = String
  type DniControlChar = String

  class DNI(number: DniNumber, letter: DniControlChar):
    require(
      letter == controlDigit(number.toInt % 23),
      "Verify the DNI. Control letter does not match the number."
    )
    override def toString: String = s"$number-$letter"
  end DNI

  def main(args: Array[DniNumber]): Unit =
    Vector(
      // requirement failed: Verify the DNI. Control letter does not match the number.
      DNI("00000001", "A"),
      // java.lang.NumberFormatException: For input string: "R"
      DNI("R", "0000001"),
      // Valid DNI
      DNI("00000001", "R")
    ).foreach(println)

  /**
   * Conclusion: Type Aliases increases code readability but do not give as any additional guaranties
   */
