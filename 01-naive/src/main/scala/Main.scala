import domain.*
import opaqueTypes.*

object Main:

  def main(args: Array[String]): Unit = {

    // Since the model is expecting opaque type,
    // we have to cast the underlying type to the opaque type
    val firstName: Name = "John".asInstanceOf[Name]
    val middleName: Name = "Stuart".asInstanceOf[Name]
    val lastName: Name = "Mill".asInstanceOf[Name]

    val holder = AccountHolder(
      firstName,
      Some(middleName),
      lastName,
      None
    )

    // Example of IBAN from the United Kingdom
    val iban: IBAN = "GB33BUKB20201555555555".asInstanceOf[IBAN]
    val balance: Balance = -10.0.asInstanceOf[Balance]
    val account = Account(holder, iban, balance)

    println(account)
  }

