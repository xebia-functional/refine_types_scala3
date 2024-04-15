package dagmendez.presentation

import scala.util.control.NoStackTrace

object F_OpaqueTypes:

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  opaque type DniNumber = String

  object DniNumber:
    inline def apply(number: String): DniNumber = number
    def parse(number: String): Either[FormatError, DniNumber] =
      Either.cond(
        number.length == 8 && number.forall(_.isDigit),
        DniNumber(number),
        FormatError("The leading 8 characters must be digits.")
      )
  end DniNumber

  opaque type DniLetter = String

  object DniLetter:
    inline def apply(letter: String): DniLetter = letter
    def parse(letter: String): Either[FormatError, DniLetter] =
      Either.cond(
        controlDigit.values.toVector.contains(letter),
        DniLetter(letter),
        FormatError("Invalid control letter.")
      )
  end DniLetter

  extension (dniValue: DniNumber | DniLetter) inline def value: String = dniValue

  case class DNI(numero: DniNumber, letra: DniLetter)
  object DNI:
    def parse(number: Either[FormatError, DniNumber], letter: Either[FormatError, DniLetter]): Either[FormatError, DNI] =
      for
        n <- number
        l <- letter
        dni <- Either.cond(
                 l.value == controlDigit(n.value.toInt % 23),
                 DNI(n, l),
                 FormatError("Verify the DNI. Control letter does not match the number.")
               )
      yield dni
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      // FormatError: Verify the DNI. Control letter does not match the number.
      DNI.parse(DniNumber.parse("00000001"), DniLetter.parse("A")),
      // FormatError: The leading 8 characters must be digits.
      DNI.parse(DniNumber.parse("R"), DniLetter.parse("00000001")),
      // FormatError: The leading 8 characters must be digits.
      DNI.parse(DniNumber.parse("123BCD78"), DniLetter.parse("A")),
      // FormatError: Invalid control letter.
      DNI.parse(DniNumber.parse("12345678"), DniLetter.parse("U")),
      // FormatError: Verify the DNI. Control letter does not match the number.
      DNI.parse(DniNumber.parse("12345678"), DniLetter.parse("B")),
      // Valid DNIs
      DNI.parse(DniNumber.parse("00000001"), DniLetter.parse("R")),
      DNI.parse(DniNumber.parse("12345678"), DniLetter.parse("Z"))
    ).foreach(println)
