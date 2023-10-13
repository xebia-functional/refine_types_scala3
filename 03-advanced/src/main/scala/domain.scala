import opaqueTypes.*
object domain:

  // The account holder is the person who signs the contract for said account with the bank
  case class AccountHolder(firstName: FirstName,
                           middleName: Option[FirstName],
                           lastName: LastName,
                           secondLastName: Option[LastName]
                          )


  case class Account(accountHolder: AccountHolder,
                     iban: IBAN // International Bank Account Number
                    )

