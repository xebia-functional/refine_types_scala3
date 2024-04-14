package dagmendez.presentation

object A_TypeAliases:

  type DniNumber      = String
  type DniControlChar = String

  class DNI private (number: DniNumber, letter: DniControlChar):
    require(letter == controlDigit(number.toInt % 23))
    def value: String = s"$number-$letter"
  end DNI
      
  object DNI:
    def fromString(dni: String): DNI =
      val dniValues: Array[DniNumber] = dni.split("-")
      val number: DniNumber = dniValues.head
      val letter: DniControlChar = dniValues(1)
      DNI(number, letter)
  end DNI
  
  @main def A_TypeAliases_Run: Unit = 
    Vector(
      DNI.fromString("00000001-R"),
      DNI.fromString("0000-001-R") // Will cause program to crash
    ).map(_.value).foreach(println)

  /**
   * Conclusion:
   * Type Aliases increases code readability but do not give as any additional guaranties 
   */