package dagmendez.iron

object E_ValueClassesWithErrorHandling:

  class Number private (val value: Int)
  object Number:
    def parse(number: Int): Either[String, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then "Number has to be positive."
        else "Maximum amount of numbers is 8."
      )

  class Letter private (val value: String)
  object Letter:
    def parse(letter: String): Either[String, Letter] =
      Either.cond(
        controlDigit.values.exists(_controlDigit => letter == _controlDigit),
        new Letter(letter),
        "Invalid control letter."
      )

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): Either[String, DNI] =
      for
        number <- Number.parse(number)
        letter <- Letter.parse(letter)
        dni <- Either.cond(
          letter.value == controlDigit(number.value % 23),
          new DNI(number, letter),
          "Control letter does not match the number."
        )
      yield dni

  val valid1 = DNI(1, "R")
  val valid2 = DNI(12345678, "Z")

  val invalidNegative            = DNI(-1, "R")
  val invalidTooLong             = DNI(1234567890, "R")
  val invalidInvalidLetter       = DNI(12345678, "Ñ")
  val invalidDoesNotMatchLetter1 = DNI(1, "A")
  val invalidDoesNotMatchLetter2 = DNI(12345678, "B")

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)
    println("== Invalid DNIs ==")
    println(invalidNegative)
    println(invalidTooLong)
    println(invalidInvalidLetter)
    println(invalidDoesNotMatchLetter1)
    println(invalidDoesNotMatchLetter2)
