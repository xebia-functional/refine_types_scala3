object opaqueTypes:



  opaque type Name = String
  opaque type IBAN  = String
  opaque type Balance = Double

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
    def apply(balance: Double): Balance = balance  