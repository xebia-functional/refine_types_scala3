package dagmendez.presentation

import scala.compiletime.ops.any.{==, ToString}
import scala.compiletime.ops.int.<
import scala.compiletime.ops.string.{Length, Matches}
import scala.compiletime.{constValue, error}

object G_OpaqueTypesWithCompileError:

  opaque type DniNumber = Int

  object DniNumber:
    inline def apply(number: Int): DniNumber =
      inline if constValue[<[Length[ToString[number.type]], 9]]
      then number
      else error("Maximum amount of numbers is 8.")
  extension (number: DniNumber) inline def value: Int = number

  opaque type DniLetter = String

  object DniLetter:
    inline def apply(letter: String): DniLetter =
      inline if constValue[Matches[ToString[letter.type], "[ABCDEFGHJKLMNPQRSTVWXYZ]{1}"]]
      then letter
      else error("Invalid control letter.")

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
      // Invalid DNIs  
      // FormatError: Verify the DNI. Control letter does not match the number.
      DNI.parse(DniNumber(1), DniLetter("A")),
      DNI.parse(DniNumber(12345678), DniLetter("B")),
      // Invalid DNIs that will not compile, thus commented
      // DNI.parse(DniNumber("R"), DniLetter("00000001"))
      // DNI.parse(DniNumber(123456789), DniLetter("A"))
      // DNI.parse(DniNumber(12345678), DniLetter("U"))
    ).map(either => either.map(dni => dni.value)).foreach(println)
