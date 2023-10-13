import domain.*
import opaqueTypes.*

object Main:

  def main(args: Array[String]): Unit = {

    // Since the model is expecting opaque type,
    // we have to cast the underlying type to the opaque type
    val firstName: FirstName = "John".asInstanceOf[FirstName]
    val middleName: FirstName = "Stuart".asInstanceOf[FirstName]
    val lastName: LastName = "Mill".asInstanceOf[LastName]

    val holder = AccountHolder(
      firstName,
      Some(middleName),
      lastName,
      None
    )

    // Example of IBAN from the United Kingdom
    val iban: IBAN = "GB33BUKB20201555555555".asInstanceOf[IBAN]
    val account = Account(holder, iban)

    println(account)
  }

