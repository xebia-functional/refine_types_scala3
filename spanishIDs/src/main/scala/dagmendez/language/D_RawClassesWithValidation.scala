package dagmendez.language

/**
 * =Regular Classes with Validation in Scala=
 *
 * Basic Syntax:
 * {{{
 *    class MyClass(val value: Int):
 *      require(boolean_condition, "Error Message")
 *      require(boolean_condition, "Error Message") // Multiple require statements can be defined
 * }}}
 *
 * '''Key Language Features Used'''
 *   - Constructor validation using 'require'
 *   - Immutable class design
 *
 * ==Pros of Class-based Validation==
 *   - Strong encapsulation of validation logic
 *   - Immutability by design
 *   - Clear separation of concerns
 *   - Can have multiple parameters
 *   - Full inheritance support
 *   - More flexible than value classes
 *
 * ==Cons of Class-based Validation==
 *   - Runtime overhead (object allocation)
 *   - Memory footprint larger than value classes
 *   - Potential performance impact with many instances
 *   - GC pressure with large numbers of objects
 */

object D_RawClassesWithValidation:

  class NieLetter(val value: String):
    require(Set("X", "Y", "Z").contains(value.toUpperCase), s"'$value' is not a valid NIE letter")

  class Number(val value: Int):
    require(value > 0, s"'$value' is negative. It must be positive")
    require(value <= 99999999, s"'$value' is too big. Max number is 99999999")

  class Letter(val value: String):
    require(
      Set("T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E").contains(value),
      s"'$value' is not a valid ID letter"
    )

  class DNI(number: Number, letter: Letter):
    override def toString: String = s"${number.value}-${letter.value}"

  class NIE(nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  val validDNI = DNI(Number(1), Letter("R"))
  val validNIE = NIE(NieLetter("X"), Number(1), Letter("R"))

  // We include lazy so the values are not initialized till they are called in the println.
  lazy val invalidNIELetter        = NIE(NieLetter("A"), Number(1), Letter("R"))
  lazy val invalidNegativeNumber   = DNI(Number(-1), Letter("R"))
  lazy val invalidTooLongNumber    = DNI(Number(1234567890), Letter("R"))
  lazy val invalidControlLetterDNI = DNI(Number(1), Letter("Ñ"))

  def main(args: Array[String]): Unit =

    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    // These won't be printed. Instead, there is going to be a StackTrace error.
    // println(invalidNIELetter)
    // println(invalidNegativeNumber)
    // println(invalidTooLongNumber)
    // println(invalidControlLetterDNI)
