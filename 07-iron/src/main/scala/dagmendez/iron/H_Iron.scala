package dagmendez.iron

import io.github.iltotore.iron.:|
import io.github.iltotore.iron.RefinedTypeOps
import io.github.iltotore.iron.autoRefine
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
  extension (number: Number) inline def value: Int = number

  opaque type ValidLetter =
    In[("A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z")] DescribedAs
      "Invalid control letter."
  opaque type Letter = String :| ValidLetter
  object Letter extends RefinedTypeOps[String, ValidLetter, Letter]
  extension (letter: Letter) inline def value: String = letter

  class DNI(number: Number, letter: Letter):
    override def toString: String = prettyDNI(number, letter)

  object DNI:
    def parse(
        possibleNumber: Either[String, Number],
        possibleLetter: Either[String, Letter]
    ): Either[String, DNI] =
      for
        number <- possibleNumber
        letter <- possibleLetter
        dni <- Either.cond(
                 letter.value == controlDigit(number.value % 23),
                 new DNI(number, letter),
                 "Control letter does not match the number."
               )
      yield dni
  end DNI

  def main(args: Array[String]): Unit =
    println("=== Compile time Validation ===")
    println("== Valid DNIs ==")
    println(DNI(Number(1), Letter("R")))
    println(DNI(Number(12345678), Letter("Z")))

    println("== Invalid DNIs ==")
    println(" * Control letter does not match the number:")
    println(DNI(Number(1), Letter("A")))
    println(DNI(Number(12345678), Letter("B")))

    // Compile time errors. If you uncomment this cases, the code won't compile
    // Negative Number:
    // println(DNI.parse(Number(-1), Letter("R")))
    // Too long number:"
    // println(DNI.parse(Number(1234567890), Letter("R")))
    // Incorrect control letter:
    // println(DNI.parse(Number(12345678), Letter("Ñ")))

    println("=== Run time Validation ===")
    println("== Valid DNIs ==")
    println(DNI.parse(Number.either(1), Letter.either("R")))
    println(DNI.parse(Number.either(12345678), Letter.either("Z")))

    println("== Invalid DNIs ==")
    println(" * Negative Number:")
    println(DNI.parse(Number.either(-1), Letter.either("R")))
    println(" * Too long number:")
    println(DNI.parse(Number.either(1234567890), Letter.either("R")))
    println(" * Incorrect control letter:")
    println(DNI.parse(Number.either(12345678), Letter.either("Ñ")))
    println(" * Control letter does not match the number:")
    println(DNI.parse(Number.either(1), Letter.either("A")))
    println(DNI.parse(Number.either(12345678), Letter.either("B")))
