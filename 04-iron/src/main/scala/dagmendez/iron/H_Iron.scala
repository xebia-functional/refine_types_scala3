package dagmendez.iron

import io.github.iltotore.iron.:|
import io.github.iltotore.iron.RefinedTypeOps
import io.github.iltotore.iron.constraint.any.DescribedAs
import io.github.iltotore.iron.constraint.any.In
import io.github.iltotore.iron.constraint.numeric.LessEqual
import io.github.iltotore.iron.constraint.numeric.Positive

object H_Iron:

  opaque type PositiveNumber   = Positive DescribedAs "Number has to be positive"
  opaque type NotTooLongNumber = LessEqual[99999999] DescribedAs "Maximum amount of numbers is 8"
  opaque type ValidNumber      = PositiveNumber & NotTooLongNumber
  opaque type Number           = Int :| ValidNumber
  object Number extends RefinedTypeOps[Int, ValidNumber, Number]
  extension (number: Number) inline def unwrap: Int = number

  opaque type ValidLetter =
    In[("A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z")] DescribedAs
      "Invalid control letter."
  opaque type Letter = String :| ValidLetter
  object Letter extends RefinedTypeOps[String, ValidLetter, Letter]
  extension (letter: Letter) inline def unwrap: String = letter

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.unwrap}-${letter.unwrap}"

  object DNI:
    def apply(number: Int, letter: String): Either[String, DNI] =
      for
        number <- Number.either(number)
        letter <- Letter.either(letter)
        dni <- Either.cond(
          letter.unwrap == controlDigit(number.unwrap % 23),
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
