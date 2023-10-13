import opaqueTypes.*

object domain:

  // The account holder is the person who signs the contract for said account with the bank
  case class AccountHolder(firstName: Name,
                           middleName: Option[Name],
                           lastName: Name,
                           secondLastName: Option[Name]
                          )


  case class Account(accountHolder: AccountHolder,
                     iban: IBAN, // International Bank Account Number
                     balance: Balance
                    )