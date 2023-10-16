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

Doesn't it look better?

So, what are the benefits of using `type aliases`? Well, our code is more readable, and we can grasp faster what is going on.
But that is about it. We can still use the underlying primitive type API on our types. We have just gain some readability.

Additional information in Alvin Alexander's [blog](https://alvinalexander.com/scala/scala-type-aliases-syntax-examples/).

## Standard



## Advanced

## Professional



https://www.tbg5-finance.org/?ibandocs.shtml

https://www.iban.com/structure