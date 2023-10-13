import domain.*
import opaqueTypes.*

object Main:

  object HappyApply:
    val firstName: FirstName = FirstName("John")
    val middleName: FirstName = FirstName("Stuart")
    val lastName: LastName = LastName("Mill")
    val iban: IBAN = IBAN("GB33BUKB20201555555555")

    val account: Account = Account(
      AccountHolder(
        firstName,
        Some(middleName),
        lastName,
        secondLastName = None
      ), iban)

    def print(): Unit = println(account)

  object UnhappyApply:
    val firstName: FirstName = FirstName("John") // Comment this one an uncomment next line
    //val firstName: FirstName = FirstName("") // Uncomment and won't compile
    val middleName: FirstName = FirstName("Stuart")
    val lastName: LastName = LastName("Mill")
    val iban: IBAN = IBAN("GB33BUKB20201555555555")

    val account: Account = Account(
      AccountHolder(
        firstName,
        Some(middleName),
        lastName,
        secondLastName = None
      ), iban)

    def print(): Unit = println(account)

  object HappyFrom:
    val firstName = FirstName.from("John")
    val middleName = FirstName.from("Stuart")
    val lastName = LastName.from("Mill")
    val iban = IBAN.from("GB33BUKB20201555555555")

    val account =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib)

    assert(account.isRight)
    def print(): Unit = println(account)

  object UnhappyFrom:

    // Play with any field that would crash the validation and return Left
    val firstName = FirstName.from("John")
    val middleName = FirstName.from("Stuart ") // This returns Left.
    val lastName = LastName.from("Mill")
    val iban = IBAN.from("GB33BUKB20201555555555")

    val account =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib)

    assert(account.isLeft)
    def print(): Unit = println(account)

  def main(args: Array[String]): Unit = {
    HappyApply.print() // Compiles
    UnhappyApply.print() // Won't compile
    HappyFrom.print() // Right
    UnhappyFrom.print() // Left
  }

