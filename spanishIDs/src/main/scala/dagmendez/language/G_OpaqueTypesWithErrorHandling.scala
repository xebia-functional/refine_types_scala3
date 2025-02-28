package dagmendez.language

/**
 * =Opaque Types with Error Handling=
 *
 * Opaque types are a Scala 3 feature that provides type abstraction without runtime overhead. It allows to create new types that are light-weighted
 * and can incorporate benefits of Value Classes and other structures.
 *
 * Basic syntax:
 * {{{
 * opaque type MyOpaqueType = UnderlyingType
 * object MyOpaqueType:
 *   def apply(underlyingValue: UnderlyingType): MyOpaqueType = underlyingValue
 *   def parse(input: UnderlyingType): Either[String, MyOpaqueType] =
 *     Either.cond(
 *       // boolean_condition
 *       MyOpaqueType(input),
 *       "Error message"
 *     )
 * }}}
 *
 * '''Key Features'''
 *   - Type safety
 *   - Validation control
 *   - Use of Either for error handling
 *
 * ==Pros of Opaque Types with Error Handling==
 *   - Performance Benefits: No runtime overhead. Opaque types do not exist during runtime.
 *   - Safety Guarantees: Compile-time type safety; runtime validation guarantees; immutable by design
 *   - Developer Experience: Clear API boundaries; self-documenting code; easy to maintain and refactor
 *   - Encapsulation: Prevent invalid states through controlled construction
 *
 * ==Cons of Value Classes with Error Handling==
 *   - Implementation Complexity: Opaque type's underlying representation is only visible in the companion object; can make debugging more challenging
 *   - Usage Restrictions: Cannot extend other classes; Limited to a single parameter; Some scenarios force boxing
 *   - Learning Curve: Requires understanding of Either type; Pattern matching knowledge needed; (Basic) Functional programming concepts required
 *   - Potential Overuse: Can lead to unnecessary abstraction if not used judiciously; might complicate simple code if used where not needed
 */

object G_OpaqueTypesWithErrorHandling:

  opaque type NieLetter = String
  opaque type Number    = Int
  opaque type Letter    = String

  object NieLetter:
    private def apply(input: String): NieLetter = input
    def either(input: String): Either[String, NieLetter] =
      Either.cond(
        Set("X", "Y", "Z").contains(input),
        NieLetter(input),
        s"'$input' is not a valid NIE letter"
      )

  object Number:
    private def apply(number: Int): Number = number
    def either(number: Int): Either[String, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        Number(number),
        if number <= 0 then s"'$number' is negative. It must be positive"
        else s"'$number' is too big. Max number is 99999999"
      )

  object Letter:
    private def apply(input: String): NieLetter = input
    def either(input: String): Either[String, Letter] =
      Either.cond(
        Set("T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E").contains(input),
        Letter(input),
        s"'$input' is not a valid DNI letter"
      )

  class DNI private (number: Number, letter: Letter):
    override def toString: String = s"$number-$letter"

  object DNI:
    def apply(number: Either[String, Number], letter: Either[String, Letter]): Either[String, DNI] =
      for
        n <- number
        l <- letter
      yield new DNI(n, l)

  class NIE private (nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"$nieLetter-$number-$letter"

  object NIE:
    def apply(nieLetter: Either[String, NieLetter], number: Either[String, Number], letter: Either[String, Letter]): Either[String, NIE] =
      for
        nl <- nieLetter
        n  <- number
        l  <- letter
      yield new NIE(nl, n, l)

  val valid1   = DNI(Number.either(1), Letter.either("R"))
  val valid2   = DNI(Number.either(12345678), Letter.either("Z"))
  val validNIE = NIE(NieLetter.either("X"), Number.either(1), Letter.either("R"))

  val invalidNIELetter     = NIE(NieLetter.either("A"), Number.either(1), Letter.either("R"))
  val invalidNegative      = DNI(Number.either(-1), Letter.either("R"))
  val invalidTooLong       = DNI(Number.either(1234567890), Letter.either("R"))
  val invalidInvalidLetter = DNI(Number.either(12345678), Letter.either("Ñ"))

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
    println("== Invalid DNIs ==")
    println(invalidNIELetter)
    println(invalidNegative)
    println(invalidTooLong)
    println(invalidInvalidLetter)
