package dagmendez.standard

import dagmendez.standard.opaqueTypes._

object domain:

  // The account holder is the person who signs the contract for said account with the bank
  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

  final case class Account(accountHolder: AccountHolder,
                           iban: IBAN, // International Bank Account Number
                           balance: Balance
  )
