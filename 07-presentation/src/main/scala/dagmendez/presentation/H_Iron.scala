package dagmendez.presentation

import io.github.iltotore.iron.constraint.any.DescribedAs
import io.github.iltotore.iron.constraint.numeric.{Positive, LessEqual}
import io.github.iltotore.iron.constraint.any.In
import io.github.iltotore.iron.{:|, RefinedTypeOps, autoRefine}
import scala.util.control.NoStackTrace
object H_Iron:

  opaque type ValidNumber = Positive & LessEqual[99999999] DescribedAs "Maximum amount of numbers is 8."
  opaque type DniNumber   = Int :| ValidNumber
  object DniNumber extends RefinedTypeOps[Int, ValidNumber, DniNumber]
  extension (number: DniNumber) inline def value: Int = number

  opaque type ValidLetter =
    In[("A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z")] DescribedAs
      "Invalid control letter."
  opaque type DniLetter = String :| ValidLetter
  object DniLetter extends RefinedTypeOps[String, ValidLetter, DniLetter]
  extension (letter: DniLetter) inline def value: String = letter

  case class DNI(number: DniNumber, letter: DniLetter):
    def value: String =
      val formatLeadingZeros = String.format("%08d", number.value)
      s"$formatLeadingZeros-$letter"

  object DNI:
    def parse(number: DniNumber, letter: DniLetter): Either[FormatError, DNI] =
      Either.cond(
        letter.value == controlDigit(number.value % 23),
        DNI(number, letter),
        FormatError("Verify the DNI. Control letter does not match the number.")
      )
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      // Valid DNIs
      DNI.parse(DniNumber(1), DniLetter("R")),
      DNI.parse(DniNumber(12345678), DniLetter("Z")),
      // FormatError: Verify the DNI. Control letter does not match the number.
      DNI.parse(DniNumber(1), DniLetter("A")),
      DNI.parse(DniNumber(12345678), DniLetter("B")),
      // Invalid DNIs that will not compile, thus commented
      // DNI.parse(DniNumber("R"), DniLetter("00000001"))
      // DNI.parse(DniNumber(123456789), DniLetter("A"))
      // DNI.parse(DniNumber(12345678), DniLetter("U"))
    ).map(either => either.map(dni => dni.value)).foreach(println)
