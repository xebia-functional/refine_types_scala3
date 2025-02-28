package dagmendez.language

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int.<=
import scala.compiletime.ops.int.>
import scala.compiletime.ops.string.Matches

/**
 * =Opaque Types with Compile-Time Error Handling=
 *
 * Opaque types provide a way to define new, distinct types without runtime overhead, and compile-time operations allow for validation of these types
 * during the compilation process, ensuring that only valid values are allowed.
 *
 * ==Introduction to Compile-Time Operations and Opaque Types==
 *
 * Compile-time operations in Scala 3, often used within `inline` methods, allow you to validate values during compilation. This leverages Scala's
 * metaprogramming capabilities to enforce constraints, reducing runtime errors by catching invalid values early. Opaque types, on the other hand,
 * help define type-safe abstractions over existing types while retaining the underlying type's performance characteristics.
 *
 * Basic Syntax:
 * {{{
 *   opaque type MyOpaqueType = UnderlyingType
 *
 *   object MyOpaqueType:
 *     inline def apply(input: UnderlyingType): MyOpaqueType =
 *       inline if constValue[ // Type Level boolean condition ]
 *       then input
 *       else error("Error message")
 * }}}
 *
 * '''Key Features'''
 *   - Compile-Time Validation: Validates values during compilation, catching errors before the code runs
 *   - Type Safety: Ensures distinct and meaningful domain types, preventing misuse of primitive types
 *   - Zero Runtime Overhead: Opaque types are erased to their underlying representation at runtime, providing type safety without performance costs
 *
 * ==Pros of Using This Approach==
 *   - Early Error Detection: Errors are caught at compile time, making the code safer and reducing the likelihood of runtime exceptions
 *   - Improved Type Safety: Strongly typed domains prevent the accidental mixing of values that share the same underlying type
 *   - Enhanced Code Clarity: Domain-specific types make the code more readable and self-documenting
 *   - No Performance Penalty: Since opaque types are erased at runtime, they do not introduce any performance overhead
 *
 * ==Cons of Using This Approach==
 *   - Increased Complexity: The use of metaprogramming and compile-time operations can make the code harder to understand and maintain
 *   - Limited Interoperability: Interfacing with libraries and frameworks expecting standard types can be more challenging
 *   - Compile-Time Overhead: The compilation process may be slower due to the additional validation checks
 *   - Learning Curve: Requires understanding of programming with types and basic metaprogramming skills
 */

object H_OpaqueTypesWithCompileError:

  opaque type NieLetter = String
  opaque type Number    = Int
  opaque type Letter    = String

  object NieLetter:
    inline def apply(input: String): NieLetter =
      inline if constValue[Matches[input.type, "[XYZ]{1}"]]
      then input
      else error("'" + constValue[input.type] + "' is not a valid NIE letter")

  object Number:
    inline def apply(number: Int): Number =
      inline if constValue[&&[      // Equal to: if (number > 0 && number <= 99999999)
          >[number.type, 0],        // Equal to: if (number > 0)
          <=[number.type, 99999999] // Equal to: if (number <= 99999999)
        ]]
      then number
      else if number <= 0 then error("'" + constValue[ToString[number.type]] + "' is negative. It must be positive")
      else error("'" + constValue[ToString[number.type]] + "' is too big. Max number is 99999999")

  object Letter:
    inline def apply(letter: String): Letter =
      inline if constValue[Matches[letter.type, "[ABCDEFGHJKLMNPQRSTVWXYZ]{1}"]] // Equal to: regex
      then letter
      else error("'" + constValue[letter.type] + "' is not a valid ID letter")

  class DNI(number: Number, letter: Letter):
    override def toString: String = s"$number-$letter"

  class NIE(nieLetter: NieLetter, number: Number, letter: Letter):
    override def toString: String = s"$nieLetter-$number-$letter"

  val valid1   = DNI(Number(1), Letter("R"))
  val valid2   = DNI(Number(12345678), Letter("Z"))
  val validNIE = NIE(NieLetter("X"), Number(1), Letter("R"))

  // The compiler will mark this values as illegal. It will give us a custom error message. Uncomment to see it.
  // val invalidNIELetter = NIE(NieLetter("A"), Number(1), Letter("R"))
  // val invalidNegative = DNI(Number(-1), Letter("R"))
  // val invalidTooLong = DNI(Number(1234567890), Letter("R"))
  // val invalidInvalidLetter = DNI(Number(12345678), Letter("Ñ"))

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid1)
    println(valid2)
    println(validNIE)
