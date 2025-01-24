package dagmendez.iron

object F_OpaqueTypes:

  opaque type Number = Int
  object Number:
    private inline def apply(number: Int): Number = number
    def parse(number: Int): Either[FormatError, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        Number(number),
        if number <= 0 then FormatError("Number has to be positive.")
        else FormatError("Maximum amount of numbers is 8.")
      )
  end Number
  extension (number: Number) inline def value: Int = number

  opaque type Letter = String
  object Letter:
    private inline def apply(letter: String): Letter = letter
    def parse(letter: String): Either[FormatError, Letter] =
      Either.cond(
        controlDigit.values.exists(_controlDigit => letter == _controlDigit),
        Letter(letter),
        FormatError("Invalid control letter.")
      )
  end Letter
  extension (letter: Letter) inline def value: String = letter

  class DNI private (number: Number, letter: Letter):
    override def toString: String = prettyDNI(number, letter)

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
