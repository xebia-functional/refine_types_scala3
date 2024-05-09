package dagmendez.presentation

object E_ValueClassesWithErrorHandling:

  class Number private (val value: Int)
  object Number:
    def parse(number: Int): Either[FormatError, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then FormatError("Number has to be positive.")
        else FormatError("Maximum amount of numbers is 8.")
      )
  end Number

  class Letter private (val value: String)
  object Letter:
    def parse(letter: String): Either[FormatError, Letter] =
      Either.cond(
        controlDigit.values.exists(_controlDigit => letter == _controlDigit),
        new Letter(letter),
        FormatError("Invalid control letter.")
      )
  end Letter

  class DNI private (number: Number, letter: Letter):
    override def toString: String =
      val numberWithLeadingZeroes = addLeadingZeroes(number.value)
      val readableDni = numberWithLeadingZeroes.concat("-").concat(letter.value)
      readableDni
    end toString
  end DNI

  object DNI:
    def parse(
               possibleNumber: Either[FormatError, Number],
               possibleLetter: Either[FormatError, Letter]
             ): Either[FormatError, DNI] =
      for
        number <- possibleNumber
        letter <- possibleLetter
        dni <- Either.cond(
                 letter.value == controlDigit(number.value % 23),
                 new DNI(number, letter),
                 FormatError("Control letter does not match the number.")
               )
      yield dni
  end DNI

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(DNI.parse(Number.parse(1), Letter.parse("R")))
    println(DNI.parse(Number.parse(12345678), Letter.parse("Z")))

    println("== Invalid DNIs ==")
    println(" * Negative Number:")
    println(DNI.parse(Number.parse(-1), Letter.parse("R")))
    println(" * Too long number:")
    println(DNI.parse(Number.parse(1234567890), Letter.parse("R")))
    println(" * Incorrect control letter:")
    println(DNI.parse(Number.parse(12345678), Letter.parse("Ñ")))
    println(" * Control letter does not match the number:")
    println(DNI.parse(Number.parse(1), Letter.parse("A")))
    println(DNI.parse(Number.parse(12345678), Letter.parse("B")))
