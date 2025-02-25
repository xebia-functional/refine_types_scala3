package dagmendez.language

/**
 * =Value Classes in Scala=
 *
 * A value class in Scala is a mechanism to define a wrapper around a single value without the runtime overhead of creating an actual instance of the
 * wrapper class.
 *
 * Basic Syntax:
 * {{{
 * class MyValueClass(val value: UnderlyingType) extends AnyVal
 * }}}
 *
 * =Pros of Value Classes=
 *
 * '''Type Safety'''
 *   - Provides compile-time type checking
 *   - Prevents mixing up different types that share the same underlying representation
 *   - Example: NieLetter, Number, and Letter can't be accidentally interchanged
 *
 * '''Zero-Cost Abstraction'''
 *   - At runtime, the JVM typically eliminates the wrapper class
 *   - No performance overhead compared to using the underlying type directly
 *
 * '''Domain Modeling'''
 *   - Helps create more meaningful domain types
 *   - Makes code more readable and self-documenting
 *   - Example: DNI and NIE classes represent Spanish identification documents
 *
 * '''Encapsulation'''
 *   - Can add methods to primitive types without inheritance
 *   - Keeps related functionality together
 *
 * =Cons of Value Classes=
 *
 * '''Limited Validation'''
 *   - Value Classes give us some enforcement of order but not much more
 *   - Can't prevent invalid values at compile time (like negative numbers or invalid letters)
 *
 * '''Restrictions'''
 *   - Can only have one parameter
 *   - Cannot have auxiliary constructors
 *   - Cannot extend other classes (except universal traits)
 *
 * '''Boxing Limitations'''
 *   - May still box in certain scenarios (collections, generic methods)
 *   - Performance benefits can be lost in these cases
 *
 * '''Limited Inheritance'''
 *   - Cannot be extended by other classes
 *   - Limited trait support
 *
 * =Practical Impact=
 * The code example shows how value classes can be used to create a type-safe representation of Spanish identification documents (DNI and NIE), but
 * also demonstrates their limitations in preventing invalid data at runtime.
 */

object C_ValueClasses:

  class NieLetter(val value: String) extends AnyVal
  class Number(val value: Int)       extends AnyVal
  class Letter(val value: String)    extends AnyVal

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  object DNI:
    def apply(number: Int, letter: String): DNI =
      new DNI(Number(number), Letter(letter))

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object NIE:
    def apply(nieLetter: String, number: Int, letter: String): NIE =
      new NIE(NieLetter(nieLetter), Number(number), Letter(letter))

  // Valid cases
  val validDNI = DNI(1, "R")
  val validNIE = NIE("X", 1, "R")

  // Invalid cases that can't be prevented at compile time
  val invalidNIELetter        = NIE("A", 1, "R")
  val invalidNegativeNumber   = DNI(-1, "R")
  val invalidTooLongNumber    = DNI(1234567890, "R")
  val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)
