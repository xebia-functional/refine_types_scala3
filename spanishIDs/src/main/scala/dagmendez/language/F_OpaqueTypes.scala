package dagmendez.language

import dagmendez.common.ControlLetter
import dagmendez.common.FailedValidation
import dagmendez.common.InvalidControlLetter
import dagmendez.common.InvalidNegativeNumber
import dagmendez.common.InvalidTooBigNumber
import dagmendez.common.NieLetter

object F_OpaqueTypes:

  opaque type Number = Int
  object Number:
    private inline def apply(number: Int): Number = number
    def either(number: Int): Either[FailedValidation, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        Number(number),
        if number <= 0 then InvalidNegativeNumber(number)
        else InvalidTooBigNumber(number)
      )
  extension (number: Number) inline def unwrap: Int = number

  class DNI private (number: Number, letter: ControlLetter):
    override def toString: String = s"${number.unwrap}-${letter}"

  object DNI:
    def apply(number: Int, letter: String): Either[FailedValidation, DNI] =
      for
        number <- Number.either(number)
        letter <- ControlLetter.either(letter)
        dni <- Either.cond(
          ControlLetter.isValidId(number, letter),
          new DNI(number, letter),
          InvalidControlLetter(letter.toString)
        )
      yield dni

  class NIE private (nieLetter: NieLetter, number: Number, letter: ControlLetter):
    override def toString: String = s"${nieLetter}-${number.unwrap}-${letter}"

  object NIE:
    def apply(nieLetter: String, number: Int, letter: String): Either[FailedValidation, NIE] =
      for
        nieLetter <- NieLetter.either(nieLetter)
        number    <- Number.either(number)
        letter    <- ControlLetter.either(letter)
        nie <- Either.cond(
          ControlLetter.isValidId(s"${nieLetter.ordinal}$number".toInt, letter),
          new NIE(nieLetter, number, letter),
          InvalidControlLetter(letter.toString)
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
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
    println("== Invalid DNIs ==")
    println(invalidNIELetter)
    println(invalidNegative)
    println(invalidTooLong)
    println(invalidInvalidLetter)
    println(invalidDoesNotMatchLetter1)
    println(invalidDoesNotMatchLetter2)
