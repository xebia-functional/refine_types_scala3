package dagmendez.language

/**
 * =Type Aliases in Scala=
 *
 * Type aliases allow you to give alternative names to existing types. They are declared using the 'type' keyword.
 *
 * Basic Syntax:
 * {{{
 *    type NewTypeName = ExistingType
 * }}}
 *
 * ==Pros of Type Aliases==
 *   - Improved Readability: Makes code more domain-specific and self-documenting by adding semantic meaning to primitive types
 *   - Reduced Verbosity: Shortens complex type signatures; especially useful for complex generic types
 *   - Maintenance Benefits: Centralizes type definitions and makes refactoring easier
 *
 * ==Cons of Type Aliases==
 *   - No Type Safety: it does not provide type checking. Hence, it can't prevent mixing of semantically different values of the same base type
 *   - Potential Confusion: May mislead developers into thinking they provide type safety; can make code more complex if overused
 */

object B_TypeAliases:

  type NieLetter = String
  type Number    = Int
  type Letter    = String

  class DNI(number: Number, letter: Letter):
    override def toString: String = s"$number-$letter"

  class NIE(nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"$nieLetter-$number-$letter"

  val validDNI = DNI(1, "R")
  val validNIE = NIE("X", 1, "R")

  // These compile despite being semantically incorrect
  val invalidNIELetter        = NIE("A", 1, "R")     // Invalid NIE letter
  val invalidNegativeNumber   = DNI(-1, "R")         // Negative number
  val invalidTooLongNumber    = DNI(1234567890, "R") // Too long number
  val invalidControlLetterDNI = DNI(1, "Ñ")          // Invalid control letter

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)
