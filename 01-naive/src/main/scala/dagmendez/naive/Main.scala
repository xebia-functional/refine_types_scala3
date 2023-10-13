package dagmendez.naive

import dagmendez.naive.domain.*
import dagmendez.naive.opaqueTypes.*

object Main:

  // Since the model is expecting opaque type,
  // we have to cast the underlying type to the opaque type
  private val firstName: Name  = "John".asInstanceOf[Name]
  private val middleName: Name = "Stuart".asInstanceOf[Name]
  private val lastName: Name   = "Mill".asInstanceOf[Name]

  private val holder = AccountHolder(
    firstName,
    Some(middleName),
    lastName,
    None
  )

  // Example of IBAN from the United Kingdom
  private val iban: IBAN       = "GB33BUKB20201555555555".asInstanceOf[IBAN]
  private val balance: Balance = -10.0.asInstanceOf[Balance]
  private val account          = Account(holder, iban, balance)

  private def print(): Unit = println(account)

  @main def run(): Unit =
    print()
