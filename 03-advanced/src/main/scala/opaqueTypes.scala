import error.*
import scala.compiletime.{codeOf,error}


object opaqueTypes:



  opaque type FirstName = String
  opaque type LastName = String
  opaque type IBAN  = String

  /*
  * Validations on the apply method are restricted to those that can be evaluated at compile time.
  * Most of the API of the basic types (String, Int, etc.) are of no use here.
  * Notice that the validations are different in the apply method and the from method.
  * Both validations should be equal and defined in a single places.
  * `scala.compiletime.codeOf` returns the value of the parameter passed into the inlined method apply
  * `scala.compiletime.error` generates a custom compiler error.
  * */

  object FirstName:

    inline def apply(fn: String): FirstName =
      inline if fn == ""
      then error(codeOf(fn) + " is invalid.")
      else fn

    def from(fn: String): Either[InvalidName, FirstName] =
      // Here we can access the underlying type API because it is evaluated during runtime.
      if fn.isBlank | (fn.trim.length < fn.length)
      then Left(InvalidName(s"First name is invalid with value <$fn>."))
      else Right(fn)

  object LastName:

    inline def apply(ln: String): LastName =
      inline if ln == ""
      then error(codeOf(ln) + " is invalid")
      else ln

    def from(ln: String): Either[InvalidName, LastName]  =
      if ln.isBlank | (ln.trim.length < ln.length)
      then Left(InvalidName(s"Last name is invalid with value <$ln>."))
      else Right(ln)

  object IBAN:

    inline def apply(iban: String): IBAN =
      inline if iban == ""
      then error(codeOf(iban) + " in invalid.")
      else iban

    def from(iban: String): Either[InvalidIBAN, IBAN] =
      if iban.isBlank | iban.contains(" ")
      then Left(InvalidIBAN(s"First name is invalid with value <$iban>."))
      else Right(iban)