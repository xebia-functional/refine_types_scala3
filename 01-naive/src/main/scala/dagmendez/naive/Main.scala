package dagmendez.naive

import dagmendez.naive.domain.*
import dagmendez.naive.typeAliases.*

object Main:

  private val firstName: Name  = "John"
  private val middleName: Name = "Stuart"
  private val lastName: Name   = "Mill"

  private val holder = AccountHolder(
    firstName,
    Some(middleName),
    lastName,
    None
  )

  // Example of IBAN from the United Kingdom
  private val iban: IBAN       = "GB33BUKB20201555555555"
  private val balance: Balance = -10.0
  private val account          = Account(holder, iban, balance)

  private def print(): Unit = println(account)

  @main def run(): Unit =
    print()
