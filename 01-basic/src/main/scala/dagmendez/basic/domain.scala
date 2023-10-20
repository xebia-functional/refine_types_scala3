package dagmendez.basic

import dagmendez.basic.typeAliases.*

object domain:

  // The account holder is the person who signs the contract for said account with the bank
  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

  final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)
