package dagmendez.a

object Basic:

  /*
   * 1. Type Aliases
   */

  type Name    = String
  type IBAN    = String // International Bank Account Number
  type Balance = Int

  /*
   * 2. Domain
   */

  // The account holder is the person who signs the contract for said account with the bank
  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

  final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)

  @main def run(): Unit =

    val firstName: Name  = "John"
    val middleName: Name = "Stuart"
    val lastName: Name   = "Mill"

    val holder = AccountHolder(
      firstName,
      Some(middleName),
      lastName,
      None
    )

    // Example of IBAN from the United Kingdom
    val iban: IBAN       = "GB33BUKB20201555555555"
    val balance: Balance = -10
    val account          = Account(holder, iban, balance)

    println(account)

end Basic
