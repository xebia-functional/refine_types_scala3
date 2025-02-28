package dagmendez.language

import dagmendez.common.FailedValidation
import dagmendez.common.InvalidControlLetter
import dagmendez.common.InvalidDniLetter
import dagmendez.common.InvalidNegativeNumber
import dagmendez.common.InvalidNieLetter
import dagmendez.common.InvalidTooBigNumber
import dagmendez.common.controlLetter

object E_ValueClassesWithErrorHandling:

  class NieLetter private (val value: String)
  object NieLetter:
    def parse(nieLetter: String): Either[InvalidNieLetter, NieLetter] =
      Either.cond(
        Set("X", "Y", "Z").contains(nieLetter.toUpperCase),
        new NieLetter(nieLetter),
        InvalidNieLetter(nieLetter)
      )

  class Number private (val value: Int)
  object Number:
    def parse(number: Int): Either[FailedValidation, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then InvalidNegativeNumber(number)
        else InvalidTooBigNumber(number)
      )

  class Letter private (val value: String)
  object Letter:
    def parse(letter: String): Either[InvalidDniLetter, Letter] =
      Either.cond(
        controlLetter.values.toSeq.contains(letter),
        new Letter(letter),
        InvalidDniLetter(letter)
      )

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): Either[FailedValidation, DNI] =
      for
        number <- Number.parse(number)
        letter <- Letter.parse(letter)
        dni <- Either.cond(
          letter.value == controlLetter(number.value % 23),
          new DNI(number, letter),
          InvalidControlLetter(letter.value)
        )
      yield dni

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object NIE:
    def apply(nieLetter: String, number: Int, letter: String): Either[FailedValidation, NIE] =
      for
        nieLetter <- NieLetter.parse(nieLetter)
        number    <- Number.parse(number)
        letter    <- Letter.parse(letter)
        nie <- Either.cond(
          letter.value == controlLetter(
            s"${nieLetter.value match
                case "X" => 0
                case "Y" => 1
                case "Z" => 2
              }${number.value}".toInt
          ),
          new NIE(nieLetter, number, letter),
          InvalidControlLetter(letter.value)
        )
      yield nie

  val valid1   = DNI(1, "R")
  val valid2   = DNI(12345678, "Z")
  val validNIE = NIE("X", 1, "R")

  val invalidNIELetter           = NIE("A", 1, "R")
  val invalidNegative            = DNI(-1, "R")
  val invalidTooLong             = DNI(1234567890, "R")
  val invalidInvalidLetter       = DNI(12345678, "Ñ")
  val invalidDoesNotMatchLetter1 = DNI(1, "A")
  val invalidDoesNotMatchLetter2 = DNI(12345678, "B")

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter.map(_.toString))
    println(invalidNegative.map(_.toString))
    println(invalidTooLong.map(_.toString))
    println(invalidInvalidLetter.map(_.toString))
    println(invalidDoesNotMatchLetter1.map(_.toString))
    println(invalidDoesNotMatchLetter2.map(_.toString))
