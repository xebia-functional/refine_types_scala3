package dagmendez.standard

import dagmendez.standard.domain.*
import dagmendez.standard.opaqueTypes.*

object Main:

  /*
   * Now we can build values of opaque types using the apply method as if they were case classes
   * Case classes create a new instance during runtime.
   * Opaque types apply methods happen during compile time.
   */

  private val firstName: Name  = Name("John")
  private val middleName: Name = Name("Stuart")
  private val lastName: Name   = Name("Mill")
  private val iban: IBAN       = IBAN("GB33BUKB20201555555555")
  private val balance: Balance = Balance(123)

  private val holder: AccountHolder =
    AccountHolder(
      firstName,
      Some(middleName),
      lastName,
      None
    )

  private val account: Account = Account(
    holder,
    iban,
    balance
  )

  private def print(): Unit = println(account)

  @main def run(): Unit = print()
