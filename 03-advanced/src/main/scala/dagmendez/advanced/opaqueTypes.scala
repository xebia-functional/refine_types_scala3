package dagmendez.advanced

import scala.compiletime.{codeOf, error}

import dagmendez.advanced.error.*

object opaqueTypes:

  opaque type Name    = String
  opaque type IBAN    = String // International Bank Account Number
  opaque type Balance = Int

  /*
   * Validations on the apply method are restricted to those that can be evaluated at compile time.
   * Most of the API of the basic types (String, Int, etc.) are of no use here.
   * Notice that the validations are different in the apply method and the from method.
   * Both validations should be equal and defined in a single places.
   * `scala.compiletime.codeOf` returns the value of the parameter passed into the inlined method apply
   * `scala.compiletime.error` generates a custom compiler error.
   * */

  object Name:

    inline def apply(name: String): Name =
      inline if name == ""
      then error(codeOf(name) + " is invalid.")
      else name

    def from(fn: String): Either[InvalidName, Name] =
      // Here we can access the underlying type API because it is evaluated during runtime.
      if fn.isBlank | (fn.trim.length < fn.length)
      then Left(InvalidName(s"First name is invalid with value <$fn>."))
      else Right(fn)

  object IBAN:

    inline def apply(iban: String): IBAN =
      inline if iban == ""
      then error(codeOf(iban) + " in invalid.")
      else iban

    def from(iban: String): Either[InvalidIBAN, IBAN] =
      if iban.isBlank | iban.contains(" ")
      then Left(InvalidIBAN(s"First name is invalid with value <$iban>."))
      else Right(iban)

  object Balance:

    inline def apply(balance: Int): Balance =
      inline if balance > 1000000 | balance < -1000
      then error(codeOf(balance) + " in invalid.")
      else balance

    def from(balance: Int): Either[InvalidBalance, Balance] =
      if balance > 1000000 | balance < -1000
      then Left(InvalidBalance(s"First name is invalid with value <$balance>."))
      else Right(balance)
