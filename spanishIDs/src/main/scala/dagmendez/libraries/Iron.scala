package dagmendez.libraries

import dagmendez.common.ControlLetter
import dagmendez.common.FailedValidation
import dagmendez.common.InvalidControlLetter

import io.github.iltotore.iron.:|
import io.github.iltotore.iron.RefinedTypeOps
import io.github.iltotore.iron.constraint.any.DescribedAs
import io.github.iltotore.iron.constraint.numeric.LessEqual
import io.github.iltotore.iron.constraint.numeric.Positive

object Iron:

  opaque type PositiveNumber   = Positive DescribedAs "Number has to be positive"
  opaque type NotTooLongNumber = LessEqual[99999999] DescribedAs "Maximum amount of numbers is 8"
  opaque type ValidNumber      = PositiveNumber & NotTooLongNumber
  opaque type Number           = Int :| ValidNumber
  object Number extends RefinedTypeOps[Int, ValidNumber, Number]
  extension (number: Number) inline def unwrap: Int = number

  class DNI private (number: Number, letter: ControlLetter):
    override def toString: String = s"${number.unwrap}-${letter}"

  object DNI:
    def apply(number: Int, letter: String): Either[String | FailedValidation, DNI] =
      for
        number <- Number.either(number)
        letter <- ControlLetter.either(letter)
        dni <- Either.cond(
          ControlLetter.isValidId(number, letter),
          new DNI(number, letter),
          InvalidControlLetter(letter.toString)
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
