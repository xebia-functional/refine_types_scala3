package dagmendez.language

/**
 * =Opaque types with Validation in Scala=
 * Opaque types allow for type abstractions without runtime overhead. They provide type safety by defining new, distinct types from existing types and
 * ensuring the correct use of these abstractions.
 *
 * Compared to type aliases, which are simply alternate names for existing types with no additional type safety, opaque types enforce stricter type
 * constraints and encapsulate the underlying type's operations and representation.
 *
 * Basic Syntax:
 * {{{
 * opaque type OpaqueType = UnderlyingType
 *
 * object OpaqueType:
 *   def apply(value: UnderlyingType): OpaqueType = value
 * }}}
 *
 * '''Key Features'''
 *   - Encapsulation: Hides the underlying type outside its defining scope.
 *   - Type Safety: Prevents accidental misuse of types, ensuring stronger type guarantees.
 *   - No Runtime Overhead: Erased to the underlying type at runtime, preserving performance.
 *
 * ==Pros of Opaque Types with Validation==
 *   - Enhanced Type Safety: Encapsulates implementation details, ensuring that only valid operations are performed on the type.
 *   - Clearer Domain Modeling: Represents domain concepts more precisely by creating new types instead of using primitive ones.
 *   - Zero Overhead: Since opaque types are erased to their underlying types at runtime, they do not introduce performance penalties.
 *
 * ==Cons of Opaque Types with Validation==
 *   - Increased Complexity: May introduce additional complexity in the type system, which can be challenging for new developers.
 *   - Limited Interoperability: Sometimes difficult to work with libraries or frameworks expecting the underlying type.
 */

object F_OpaqueTypesWithValidation:

  opaque type NieLetter = String
  opaque type Number    = Int
  opaque type Letter    = String

  object NieLetter:
    def apply(input: String): NieLetter =
      require(Set("X", "Y", "Z").contains(input))
      input

  object Number:
    def apply(input: Int): Number =
      require(input > 0)
      require(input <= 99999999)
      input

  object Letter:
    def apply(input: String): NieLetter =
      require(Set("T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E").contains(input))
      input

  class DNI(number: Number, letter: Letter):
    override def toString: String = s"$number-$letter"

  class NIE(nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"$nieLetter-$number-$letter"

  val valid1   = DNI(Number(1), Letter("R"))
  val valid2   = DNI(Number(12345678), Letter("Z"))
  val validNIE = NIE(NieLetter("X"), Number(1), Letter("R"))

  lazy val invalidNIELetter           = NIE(NieLetter("A"), Number(1), Letter("R"))
  lazy val invalidNegative            = DNI(Number(-1), Letter("R"))
  lazy val invalidTooLong             = DNI(Number(1234567890), Letter("R"))
  lazy val invalidInvalidLetter       = DNI(Number(12345678), Letter("Ñ"))
  lazy val invalidDoesNotMatchLetter1 = DNI(Number(1), Letter("A"))
  lazy val invalidDoesNotMatchLetter2 = DNI(Number(12345678), Letter("B"))

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
    // If you uncomment the print statement, a compiler error will be thrown
    println("== Invalid DNIs ==")
    // println(invalidNIELetter)
    // println(invalidNegative)
    // println(invalidTooLong)
    // println(invalidInvalidLetter)
    // println(invalidDoesNotMatchLetter1)
    // println(invalidDoesNotMatchLetter2)
