package dagmendez.professional

import scala.util.control.NoStackTrace

import dagmendez.professional.domain.*
import dagmendez.professional.opaqueTypes.*

object Main:

  private object HappyApply:
    private val firstName: Name  = Name("John")
    private val middleName: Name = Name("Stuart T")
    private val lastName: Name   = Name("Mill")
    private val iban: IBAN       = IBAN("ES451234567890123456789012")
    private val balance: Balance = Balance(123.45)

    private val account: Account = Account(
      AccountHolder(
        firstName,
        Some(middleName),
        lastName,
        secondLastName = None
      ),
      iban,
      balance
    )

    def print(): Unit = println(account)

  private object UnhappyApply:

    private val firstName: Name = Name("Jose") // Comment this one an uncomment next line
    // private val firstName: FirstName = FirstName("") // Uncomment and won't compile
    private val middleName: Name = Name("Maria")
    private val lastName: Name   = Name("Garcia")
    private val iban: IBAN       = IBAN("ES451234567890123456789012") // Comment this one an uncomment next line
    // private val iban: IBAN = IBAN("ES4512345678901234567890") // Uncomment and won't compile
    // private val balance: Balance = Balance(-3000.0) // Won't compile
    private val balance: Balance = Balance(-100.0) // Compiles

  private object HappyFrom:
    private val firstName  = Name.from("Juan")
    private val middleName = Name.from("Manuel")
    private val lastName   = Name.from("Herrero")
    private val iban       = IBAN.from("ES451234567890123456789012")
    private val balance    = Balance.from(15.60)

    private val account: Either[RuntimeException with NoStackTrace, Account] =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
        ba <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, ba)

    assert(account.isRight)
    def print(): Unit = println(account)

  private object UnhappyFrom:
    // Play with any field that would crash the validation and return Left
    private val firstName      = Name.from("Juana")
    private val middleName     = Name.from("Manuela")
    private val lastName       = Name.from("Herrero")
    private val secondLastName = Name.from("Garcia")
    private val iban           = IBAN.from("ES451234567890123456789012")
    private val balance        = Balance.from(5000000.00) // Left

    private val account: Either[RuntimeException with NoStackTrace, Account] =
      for
        fn  <- firstName
        mn  <- middleName
        ln  <- lastName
        sln <- secondLastName
        ib  <- iban
        ba  <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, Some(sln)), ib, ba)

    assert(account.isLeft)
    def print(): Unit = println(account)

  @main def run(): Unit =
    HappyApply.print()  // Compiles
    HappyFrom.print()   // Right
    UnhappyFrom.print() // Left
