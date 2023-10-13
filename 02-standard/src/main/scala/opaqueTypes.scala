object opaqueTypes:



  opaque type FirstName = String
  opaque type LastName = String
  opaque type IBAN  = String

  /*
  * Opaque types can have companion objects.
  * This allows us to provide the opaque type with an API.
  * Here we will include the apply method so we can build values of the opaque type safely.
  */

  object FirstName:
    def apply(fn: String): FirstName = fn

  object LastName:
    def apply(ln: String): LastName = ln

  object IBAN:
    def apply(iban: String): IBAN = iban