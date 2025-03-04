package dagmendez.language

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int.<=
import scala.compiletime.ops.int.>
import scala.compiletime.ops.string.Matches

import dagmendez.common.controlLetter

object G_OpaqueTypesWithCompileError:

  opaque type Number = Int
  object Number:
    inline def apply(number: Int): Number =
      inline if constValue[&&[      // Equal to: if (number > 0 && number <= 99999999)
          >[number.type, 0],        // Equal to: if (number > 0)
          <=[number.type, 99999999] // Equal to: if (number <= 99999999)
        ]]
      then number
      else if number <= 0 then error("Number has to be positive.")
      else error("Maximum amount of numbers is 8.")
  extension (number: Number) inline def unwrap: Int = number

  opaque type Letter = String

  object Letter:
    inline def apply(letter: String): Letter =
      inline if constValue[Matches[letter.type, "[ABCDEFGHJKLMNPQRSTVWXYZ]{1}"]] // Equal to: regex
      then letter
      else error("Invalid control letter.")

  extension (letter: Letter) inline def unwrap: String = letter

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.unwrap}-${letter.unwrap}"

  object DNI:
    // Parameters are passed as opaque types because the values have to be known at compile time to be validated
    def apply(number: Number, letter: Letter): Either[String, DNI] =
      Either.cond(
        letter.unwrap == controlLetter(number.unwrap % 23),
        new DNI(number, letter),
        "Control letter does not match the number."
      )

  val valid1 = DNI(Number(1), Letter("R"))
  val valid2 = DNI(Number(12345678), Letter("Z"))

  val invalidDoesNotMatchLetter1 = DNI(Number(1), Letter("A"))
  val invalidDoesNotMatchLetter2 = DNI(Number(12345678), Letter("B"))

  // Compile time errors. If you uncomment this cases, the code won't compile
  // val invalidNegative = DNI(Number(-1), Letter("R"))
  // val invalidTooLong = DNI(Number(1234567890), Letter("R"))
  // val invalidInvalidLetter = DNI(Number(12345678), Letter("Ñ"))

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)

    println("== Invalid DNIs ==")
    println(invalidDoesNotMatchLetter1)
    println(invalidDoesNotMatchLetter2)
