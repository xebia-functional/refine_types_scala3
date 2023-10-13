import domain.*
import opaqueTypes.*

object Main:

  object HappyApply:
    val firstName: Name = Name("John")
    val middleName: Name = Name("Stuart T")
    val lastName: Name = Name("Mill")
    val iban: IBAN = IBAN("ES451234567890123456789012")
    val balance: Balance = Balance(123.45)

    val account: Account = Account(
      AccountHolder(
        firstName,
        Some(middleName),
        lastName,
        secondLastName = None
      ), iban, balance)

    def print(): Unit = println(account)

  object UnhappyApply:

    val firstName: Name = Name("John") // Comment this one an uncomment next line
    //val firstName: FirstName = FirstName("") // Uncomment and won't compile
    val middleName: Name = Name("Stuart")
    val lastName: Name = Name("Mill")
    val iban: IBAN = IBAN("ES451234567890123456789012") // Comment this one an uncomment next line
    //val iban: IBAN = IBAN("ES4512345678901234567890") // Uncomment and won't compile
    //val balance: Balance = Balance(-3000.0) // Won't compile
    val balance: Balance = Balance(-100.0) // Compiles


  object HappyFrom:
    val firstName = Name.from("John")
    val middleName = Name.from("Stuart")
    val lastName = Name.from("Mill")
    val iban = IBAN.from("ES451234567890123456789012")
    val balance = Balance.from(15.60)

    val account =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
        ba <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, ba)

    assert(account.isRight)
    def print(): Unit = println(account)

  object UnhappyFrom:

    // Play with any field that would crash the validation and return Left
    val firstName = Name.from("John")
    val middleName = Name.from("Stuart")
    val lastName = Name.from("Mill")
    val iban = IBAN.from("ES451234567890123456789012")
    val balance = Balance.from(5000000.00) // Left

    val account =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
        ba <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, ba)

    assert(account.isLeft)
    def print(): Unit = println(account)

  def main(args: Array[String]): Unit = {
    HappyApply.print() // Compiles
    HappyFrom.print() // Right
    UnhappyFrom.print() // Left
  }

