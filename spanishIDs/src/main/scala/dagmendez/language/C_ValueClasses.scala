package dagmendez.language

/**
 * =Value Classes in Scala=
 *
 * A value class in Scala is a mechanism to define a wrapper around a single value without the runtime overhead of creating an actual instance of the
 * wrapper class. For a regular class to become a value class, it must contain only one parameter and extend `AnyVal`.
 *
 * Basic Syntax:
 * {{{
 * class MyValueClass(val value: UnderlyingType) extends AnyVal
 * }}}
 *
 * ==Key Features==
 *   - Can only wrap one value
 *   - Creates effectively a new type by masking the underlying type
 *   - Zero-Cost Abstraction
 *
 * ==Pros of Value Classes==
 *   - Type Safety: Provides compile-time type checking by preventing mixing up different types that share the same underlying representation
 *   - Zero-Cost Abstraction: Eliminates the wrapper class at runtime, resulting in no performance overhead compared to using the underlying type
 *     directly
 *   - Domain Modeling: Helps create more meaningful domain types and makes the code more readable and self-documenting
 *   - Encapsulation: Allows the addition of methods to primitive types without the need for inheritance and keeps related functionality together
 *     within the wrapper class
 *
 * ==Cons of Value Classes==
 *   - Limited Validation: Provides some enforcement of order but not much more. Cannot prevent invalid values at compile time
 *   - Restrictions: Can only have one parameter; cannot have auxiliary constructors; cannot extend other classes (except for universal traits)
 *   - Boxing Limitations: Performance benefits can be lost if boxing is needed, such as for collections or generic methods
 *   - Limited Inheritance: Cannot be extended by other classes and has limited support for traits
 */

object C_ValueClasses:

  class NieLetter(val value: String) extends AnyVal
  class Number(val value: Int)       extends AnyVal
  class Letter(val value: String)    extends AnyVal

  class DNI(number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  class NIE(nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  // Valid cases
  val validDNI = DNI(Number(1), Letter("R"))
  val validNIE = NIE(NieLetter("X"), Number(1), Letter("R"))

  // Invalid cases that can't be prevented at compile time
  val invalidNIELetter        = NIE(NieLetter("A"), Number(1), Letter("R"))
  val invalidNegativeNumber   = DNI(Number(-1), Letter("R"))
  val invalidTooLongNumber    = DNI(Number(1234567890), Letter("R"))
  val invalidControlLetterDNI = DNI(Number(1), Letter("Ñ"))

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)
