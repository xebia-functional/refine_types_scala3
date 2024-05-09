package dagmendez.presentation

object B_TypeAliases:

  type Number = Int
  type Letter = String

  class DNI(number: Number, letter: Letter):

    override def toString: String =
      val numberWithLeadingZeroes = addLeadingZeroes(number)
      val readableDni             = numberWithLeadingZeroes.concat("-").concat(letter)
      readableDni
    end toString

  end DNI

  def main(args: Array[String]): Unit =

    println("== Valid DNIs ==")
    println(DNI(1, "R"))

    println("== Invalid DNIs ==")
    println(" * Negative Number:")
    println(DNI(-1, "R"))
    println(" * Too long number:")
    println(DNI(1234567890, "R"))
    println(" * Incorrect control letter:")
    println(DNI(1, "Ñ"))

  /**
   * Conclusion: Type Aliases increases code readability but do not give as any additional guaranties
   */
