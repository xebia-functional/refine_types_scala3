package dagmendez.language

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
 *   - Performance Benefits: No runtime overhead at instantiation; avoids boxing/unboxing in most cases; memory efficient compared to regular classes
 *   - Safety Guarantees: Compile-time type safety; runtime validation guarantees; immutable by design
 *   - Developer Experience: Clear API boundaries; self-documenting code; easy to maintain and refactor
 *
 * ==Cons of Value Classes with Error Handling==
 *   - Implementation Complexity: Requires more initial setup code
 *   - Usage Restrictions: Cannot extend other classes; Limited to a single parameter; Some scenarios force boxing
 *   - Learning Curve: Requires understanding of Either type; Pattern matching knowledge needed; (Basic) Functional programming concepts required
 */

object F_ValueClassesWithErrorHandling:

  class NieLetter private (val value: String) extends AnyVal
  object NieLetter:
    def parse(nieLetter: String): Either[String, NieLetter] =
      Either.cond(
        Set("X", "Y", "Z").contains(nieLetter),
        new NieLetter(nieLetter),
        s"'$nieLetter' is not a valid NIE letter"
      )

  class Number private (val value: Int) extends AnyVal
  object Number:
    def parse(number: Int): Either[String, Number] =
      Either.cond(
        number >= 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then s"'$number' is negative. It must be positive"
        else s"'$number' is too big. Max number is 99999999"
      )

  class Letter private (val value: String) extends AnyVal
  object Letter:
    def parse(letter: String): Either[String, Letter] =
      Either.cond(
        Set("T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E").contains(letter),
        new Letter(letter),
        s"'$letter' is not a valid ID letter"
      )

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Either[String, Number], letter: Either[String, Letter]): Either[String, DNI] =
      for
        n <- number
        l <- letter
      yield new DNI(n, l)

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object NIE:
    def apply(nieLetter: Either[String, NieLetter], number: Either[String, Number], letter: Either[String, Letter]): Either[String, NIE] =
      for
        nl <- nieLetter
        n  <- number
        l  <- letter
      yield new NIE(nl, n, l)

  val valid1   = DNI(Number.parse(1), Letter.parse("R"))
  val valid2   = DNI(Number.parse(12345678), Letter.parse("Z"))
  val validNIE = NIE(NieLetter.parse("X"), Number.parse(1), Letter.parse("R"))

  val invalidNIELetter     = NIE(NieLetter.parse("A"), Number.parse(1), Letter.parse("R"))
  val invalidNegative      = DNI(Number.parse(-1), Letter.parse("R"))
  val invalidTooLong       = DNI(Number.parse(1234567890), Letter.parse("R"))
  val invalidInvalidLetter = DNI(Number.parse(12345678), Letter.parse("Ñ"))

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
