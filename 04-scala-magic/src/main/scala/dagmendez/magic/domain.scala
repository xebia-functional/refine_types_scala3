package dagmendez.magic

import dagmendez.magic.opaqueTypes._

object domain:

  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

  final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)
