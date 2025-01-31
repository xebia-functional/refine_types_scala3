package dagmendez.b

object Standard:

  /*
   * 1. Opaque Tyoes
   */

  opaque type Name    = String
  opaque type IBAN    = String
  opaque type Balance = Int

  /*
   * Opaque types can have companion objects.
   * This allows us to provide the opaque type with an API.
   * Here we will include the apply method so we can build values of the opaque type safely.
   */

  object Name:
    def apply(name: String): Name = name

  object IBAN:
    def apply(iban: String): IBAN = iban

  object Balance:
    def apply(balance: Int): Balance = balance

  /*
   * 2. Domain
   */

  // The account holder is the person who signs the contract for said account with the bank
  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

  final case class Account(
      accountHolder: AccountHolder,
      iban: IBAN, // International Bank Account Number
      balance: Balance
  )

  @main def run(): Unit =

    /*
     * Now we can build values of opaque types using the apply method as if they were case classes.
     * Case classes create a new instance during runtime.
     * Opaque types apply methods happen during compile time.
     */

    val firstName: Name  = Name("John")
    val middleName: Name = Name("Stuart")
    val lastName: Name   = Name("Mill")
    val iban: IBAN       = IBAN("GB33BUKB20201555555555")
    val balance: Balance = Balance(123)

    val holder: AccountHolder =
      AccountHolder(
        firstName,
        Some(middleName),
        lastName,
        None
      )

    val account: Account = Account(
      holder,
      iban,
      balance
    )

    println(account)

end Standard
