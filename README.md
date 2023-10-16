# Opaque Types and Inline

What are the possibilities of Scala 3 features `opaque type` and `inline` for type refinement?

This repository covers 4 levels of complexity:
- [Naive](#naive)
- [Standard](#standard)
- [Advanced](#advanced)
- [Professional](#professional)

The use case is based on a very simplistic model for a financial institution.

```scala 3
final case class AccountHolder(firstName: String, middleName: Option[String], lastName: String, secondLastName: Option[String])

final case class Account(accountHolder: AccountHolder, iban: String, balance: Double)
```

How can we model _better_ this domain? The primitive types do not help us much. Let's dive into the Scala 3 type system.

## Naive

[Source Code](./01-naive/src/main/scala/dagmendez/naive)

The naive way will be to declare just some `type aliases` for the primitive types. It works the same in Scala 2. For example:
```scala 3
  type Name    = String
  type IBAN    = String // International Bank Account Number
  type Balance = Double
```

With these type aliases we could redefine our basic model to:

```scala 3
final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)
```

Doesn't it look better? Now we can create an instance like this:

```scala 3
private val firstName: Name  = "John"
private val middleName: Name = "Stuart"
private val lastName: Name   = "Mill"

private val holder = AccountHolder(
  firstName,
  Some(middleName),
  lastName,
  None
)

private val iban: IBAN       = "GB33BUKB20201555555555"
private val balance: Balance = -10.0
private val account          = Account(holder, iban, balance)
```

So, what are the benefits of using `type aliases`? Well, our code is more readable, and we can grasp faster what is going on.
But that is about it. We can still use the underlying primitive type API on our types. We have just gain some readability.

Additional information in Alvin Alexander's [blog](https://alvinalexander.com/scala/scala-type-aliases-syntax-examples/).

## Standard

[Source Code](./02-standard/src/main/scala/dagmendez/standard)

Scala 3 includes a new way of declaring types that is _cheaper_ in terms of overhead. 
Just add the soft keyword `opaque` in front of type. Now, the compiler only sees the opaque type during compilation.
Thus, it does not know which is the underlying type until it compiles the code.
This prevents us from accessing the API of the underlying primitive type and pushes as into creating our own API.

```scala 3
opaque type Name    = String
opaque type IBAN    = String
opaque type Balance = Double
```

But, since the compiler does not see the underlying type... how do you create values of the `opaque type`? 
Whit an apply method in the companion object.

```scala 3
object Name:
  def apply(name: String): Name = name

object IBAN:
  def apply(iban: String): IBAN = iban

object Balance:
  def apply(balance: Double): Balance = balance
```

The creation of the instance is quite similar to the previous "naive" way. 
For those unaware of the `opaque types`, this code could make them think that we are using there `case class`(es) as wrappers of primitive types.

```scala 3
private val firstName: Name  = Name("John")
private val middleName: Name = Name("Stuart")
private val lastName: Name   = Name("Mill")
private val iban: IBAN       = IBAN("GB33BUKB20201555555555")
private val balance: Balance = Balance(123.45)

private val holder: AccountHolder =
  AccountHolder(
    firstName,
    Some(middleName),
    lastName,
    None
  )

private val account: Account = Account(
  holder,
  iban,
  balance
)
```

But we are not using `case class`(es). 
Once the code is compiled, those opaque types will be represented as their underlying type.
There is no need for the creation of an instance of a class wrapper. 
That is the main benefit of this approach in Scala 3.

Additional information in the [Scala 3 Documentation](https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html), and [Alvin Alexander's blog](https://alvinalexander.com/scala/scala-3-opaque-types-how-to/).

## Advanced



## Professional



https://www.tbg5-finance.org/?ibandocs.shtml

https://www.iban.com/structure