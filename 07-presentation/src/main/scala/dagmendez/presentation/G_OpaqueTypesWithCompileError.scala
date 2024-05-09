package dagmendez.presentation

import scala.compiletime.ops.any.==
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int.{>, <=}
import scala.compiletime.ops.string.Matches
import scala.compiletime.{constValue, error}

object G_OpaqueTypesWithCompileError:

  opaque type Number = Int

  object Number:
    inline def apply(number: Int): Number =
      inline if constValue[&&[ // Equal to: if (number > 0 && number <= 99999999)
          >[number.type, 0], // Equal to: if (number > 0)
          <=[number.type, 99999999] // Equal to: if (number <= 99999999)
        ]] 
      then number
      else if number <= 0 then error("Number has to be positive.")
      else error("Maximum amount of numbers is 8.")
  extension (number: Number) inline def value: Int = number

  opaque type Letter = String

  object Letter:
    inline def apply(letter: String): Letter =
      inline if constValue[Matches[letter.type, "[ABCDEFGHJKLMNPQRSTVWXYZ]{1}"]] // Equal to: regex
      then letter
      else error("Invalid control letter.")

  extension (letter: Letter) inline def value: String = letter

  class DNI private (number: Number, letter: Letter):
    override def toString: String =
      val numberWithLeadingZeroes = addLeadingZeroes(number.value)
      val readableDni = numberWithLeadingZeroes.concat("-").concat(letter.value)
      readableDni
    end toString
  end DNI
  
  object DNI:
    def parse(number: Number, letter: Letter): Either[FormatError, DNI] =
      Either.cond(
        letter.value == controlDigit(number.value % 23),
        DNI(number, letter),
        FormatError("Control letter does not match the number.")
      )
  end DNI

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(DNI.parse(Number(1), Letter("R")))
    println(DNI.parse(Number(12345678), Letter("Z")))

    println("== Invalid DNIs ==")
    println(" * Control letter does not match the number:")
    println(DNI.parse(Number(1), Letter("A")))
    println(DNI.parse(Number(12345678), Letter("B")))

    // Compile time errors. If you uncomment this cases, the code won't compile
    // Negative Number:
    //println(DNI.parse(Number(-1), Letter("R")))
    // Too long number:"
    //println(DNI.parse(Number(1234567890), Letter("R")))
    // Incorrect control letter:
    //println(DNI.parse(Number(12345678), Letter("Ñ")))

