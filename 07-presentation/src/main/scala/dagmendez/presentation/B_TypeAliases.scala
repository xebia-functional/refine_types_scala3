package dagmendez.presentation

object B_TypeAliases:

  type DniNumber      = String
  type DniLetter = String

  class DNI(number: DniNumber, letter: DniLetter):
    override def toString: String = s"$number-$letter"


  def main(args: Array[DniNumber]): Unit =
    Vector(
      // Valid DNI
      DNI("00000001", "R"),
      // Invalid DNIs
      DNI("00000001", "A"),
      DNI("R", "0000001")
    ).foreach(println)

  /**
   * Conclusion: Type Aliases increases code readability but do not give as any additional guaranties
   */
