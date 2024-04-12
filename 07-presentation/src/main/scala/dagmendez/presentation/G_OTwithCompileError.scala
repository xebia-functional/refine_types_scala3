package dagmendez.presentation

import scala.util.control.NoStackTrace
import scala.compiletime.constValue
import scala.compiletime.ops.string.{Length, Matches}
import scala.compiletime.ops.any.{==, ToString}
import scala.compiletime.ops.boolean.&&
import scala.compiletime.error

object G_OTwithCompileError:
  
  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  opaque type DniNumber = String

  object DniNumber:
    inline def apply(number: String): DniNumber =
      inline if constValue[&&[Length[number.type] == 8, Matches[number.type, "[0-9]*"]]] 
      then number
      else error("El número ha de contener 8 caracteres numéricos")

  extension (dniNumber: DniNumber)
    inline def value: String = dniNumber


  opaque type DniControlChar = Char

  object DniControlChar:
    inline def apply(char: Char): DniControlChar =
      inline if constValue[Matches[ToString[char.type], "[ABCDEFGHJKLMNPQRSTWXYZ]{1}"]] 
      then char 
      else error("Letra de control no válida") 

  extension (dniChar: DniControlChar)
    inline def value: Char = dniChar


  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.value}-${letra.value}"


  @main def compileRun: Unit =
    Vector(
      // Error de compilación en número
      //DNI(DniNumber("12345"), DniControlChar('A')),
      // Error compilación en letra de control
      //DNI(DniNumber("12345678"), DniControlChar('d')),
      DNI(DniNumber("12345678"), DniControlChar('D'))
    ).map(_.value).foreach(println)