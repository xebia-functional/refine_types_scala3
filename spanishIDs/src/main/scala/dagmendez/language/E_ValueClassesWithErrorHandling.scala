package dagmendez.language

import dagmendez.common.FailedValidation
import dagmendez.common.InvalidControlLetter
import dagmendez.common.InvalidDniLetter
import dagmendez.common.InvalidNegativeNumber
import dagmendez.common.InvalidNieLetter
import dagmendez.common.InvalidTooBigNumber
import dagmendez.common.controlLetter

/**
 * =Value Classes with Error Handling in Scala=
 *
 * Value Classes provide a way to create type-safe wrappers around primitive types while maintaining runtime efficiency. When combined with companion
 * objects and error handling, they offer a robust way to validate data at creation time. They are particularly useful in domains where data
 * validation is crucial. This pattern helps catch errors early in the development cycle and provides clear feedback about validation failures, making
 * systems more maintainable and reliable.
 *
 * Basic Syntax:
 * {{{
 * class ValueClass private (val value: Type) extends AnyVal
 *
 * object ValueClass:
 *   def apply(value: Type): Either[Error, ValueClassType] =
 *     Either.cond(
 *       boolean_condition,
 *       new ValueClass(value),
 *       Error("Error message")
 *     )
 * }}}
 *
 * '''Key Features'''
 *   - Type safety
 *   - Validation control
 *   - Use of Either for error handling
 *
 * ==Pros of Value Classes with Error Handling==
 *
 * '''Performance Benefits'''
 *   - No runtime overhead at instantiation
 *   - Avoids boxing/unboxing in most cases
 *   - Memory efficient compared to regular classes
 *
 * '''Safety Guarantees'''
 *   - Compile-time type safety
 *   - Runtime validation guarantees
 *   - Immutable by design
 *
 * '''Developer Experience'''
 *   - Clear API boundaries
 *   - Self-documenting code
 *   - Easy to maintain and refactor
 *
 * ==Cons of Value Classes with Error Handling==
 *
 * '''Implementation Complexity'''
 *   - Requires more initial setup code
 *   - Need to define custom error types // Optional
 *   - May require additional helper methods
 *
 * '''Usage Restrictions'''
 *   - Cannot extend other classes
 *   - Limited to a single parameter
 *   - Some scenarios force boxing
 *
 * '''Learning Curve'''
 *   - Requires understanding of Either type
 *   - Pattern matching knowledge needed
 *   - (Basic) Functional programming concepts required
 */

object E_ValueClassesWithErrorHandling:

  class NieLetter private (val value: String) extends AnyVal
  object NieLetter:
    def parse(nieLetter: String): Either[InvalidNieLetter, NieLetter] =
      Either.cond(
        Set("X", "Y", "Z").contains(nieLetter.toUpperCase),
        new NieLetter(nieLetter),
        InvalidNieLetter(nieLetter)
      )

  class Number private (val value: Int) extends AnyVal
  object Number:
    def parse(number: Int): Either[FailedValidation, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then InvalidNegativeNumber(number)
        else InvalidTooBigNumber(number)
      )

  class Letter private (val value: String) extends AnyVal
  object Letter:
    def parse(letter: String): Either[InvalidDniLetter, Letter] =
      Either.cond(
        controlLetter.values.toSeq.contains(letter),
        new Letter(letter),
        InvalidDniLetter(letter)
      )

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): Either[FailedValidation, DNI] =
      for
        number <- Number.parse(number)
        letter <- Letter.parse(letter)
        dni <- Either.cond(
          letter.value == controlLetter(number.value % 23),
          new DNI(number, letter),
          InvalidControlLetter(letter.value)
        )
      yield dni

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object NIE:
    def apply(nieLetter: String, number: Int, letter: String): Either[FailedValidation, NIE] =
      for
        nieLetter <- NieLetter.parse(nieLetter)
        number    <- Number.parse(number)
        letter    <- Letter.parse(letter)
        nie <- Either.cond(
          letter.value == controlLetter(
            s"${nieLetter.value match
                case "X" => 0
                case "Y" => 1
                case "Z" => 2
              }${number.value}".toInt
          ),
          new NIE(nieLetter, number, letter),
          InvalidControlLetter(letter.value)
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
    println("== Valid IDs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter.map(_.toString))
    println(invalidNegative.map(_.toString))
    println(invalidTooLong.map(_.toString))
    println(invalidInvalidLetter.map(_.toString))
    println(invalidDoesNotMatchLetter1.map(_.toString))
    println(invalidDoesNotMatchLetter2.map(_.toString))
