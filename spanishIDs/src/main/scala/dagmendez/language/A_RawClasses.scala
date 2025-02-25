package dagmendez.language

/**
 * SCALA REGULAR CLASS DOCUMENTATION
 *
 * In Scala, a regular class is defined using the 'class' keyword. Below are examples of DNI (Spanish National ID) and NIE (Foreign ID) classes
 *
 * Key Features of Regular Classes:
 *   - Constructor parameters are defined directly in the class declaration
 *   - Classes can have methods, fields, and other members
 *   - Classes support method overriding using 'override' keyword
 *
 * Advantages of Regular Classes: 
 *   - Straightforward object-oriented programming
 *   - Full support for inheritance and polymorphism
 *   - Encapsulation of data and behavior
 *   - Flexibility in defining custom methods and fields
 *   - Support for constructor parameters with default values
 *
 * Disadvantages of Regular Classes:
 *   - Each instance creates a new object in memory
 *   - Can't be used as type aliases (unlike case classes)
 *   - No built-in equals, hashCode, or toString methods (need manual implementation)
 *   - More verbose compared to case classes for data containers
 *   - No automatic pattern matching support
 */

object A_RawClasses:

  /**
   * Regular class representing Spanish National ID (DNI)
   *
   * @param number
   *   The numeric part of the DNI
   * @param letter
   *   The control letter
   */
  class DNI(number: Int, letter: String):
    override def toString: String = s"$number-$letter"

  /**
   * Regular class representing Foreign ID (NIE)
   *
   * @param nieLetter
   *   The initial letter (X, Y, or Z)
   * @param number
   *   The numeric part
   * @param letter
   *   The control letter
   */
  class NIE(nieLetter: String, number: Int, letter: String):
    override def toString: String = s"$nieLetter-$number-$letter"

  // Example valid and invalid instances
  val validDNI = DNI(1, "R")
  val validNIE = NIE("X", 1, "R")

  val invalidNIELetter        = NIE("A", 1, "R")     // Invalid: NIE must start with X, Y, or Z
  val invalidNegativeNumber   = DNI(-1, "R")         // Invalid: Negative numbers not allowed
  val invalidTooLongNumber    = DNI(1234567890, "R") // Invalid: Number too long
  val invalidControlLetterDNI = DNI(1, "Ñ")          // Invalid: Special characters not allowed

  def main(args: Array[String]): Unit =
    println("== Valid IDs ==")
    println(validDNI)
    println(validNIE)
    println("== Invalid IDs ==")
    println(invalidNIELetter)
    println(invalidNegativeNumber)
    println(invalidTooLongNumber)
    println(invalidControlLetterDNI)
