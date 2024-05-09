package dagmendez.presentation

object A_RawClasses:

  class DNI(number: Int, letter: String):

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
