eDSL using State monad from Scalaz
==================================

This is an example converted to Scala from Haskell. The original example:

http://lpenz.github.com/articles/hedsl-sharedexpenses/index.html

An example
----------

    val dexter = Person("Dexter")
    val angel  = Person("Angel")
    val debra  = Person("Debra")
    val harry  = Person("Harry")

    val trip = sharedexpenses ( implicit s => for {
      _ <- dexter spent 5300
      _ <- angel  spent 2700
      _ <- debra  spent  800
      _ <- harry  spent 1900
      _ <- debra  spent 1700
      _ <- angel  spent 2200
      _ <- dexter gave  (harry, 2000)
      x <- angel  gave  (debra, 3200)
    } yield x)

    scala> println(solve(trip).mkString("\n"))

    Debra pays 4350 to Angel
    Harry pays 3650 to Dexter
    Harry pays 100 to Angel

Conclusion
----------

Scala's for-comprehension is not quite as nice as Haskell's do-notation in this example. There's
some syntactic noise coming from required binding '_ <- ' and from required value yielding.
It would be perhaps possible to remove those if Scala would add some sugar to its for-comprehension:

* Do not require variable binding for M[Unit]
* yield the value of a last expression if yield is missing

After that the example would look as:

    val dexter = Person("Dexter")
    val angel  = Person("Angel")
    val debra  = Person("Debra")
    val harry  = Person("Harry")

    val trip = sharedexpenses ( implicit s => for {
      dexter spent 5300
      angel  spent 2700
      debra  spent  800
      harry  spent 1900
      debra  spent 1700
      angel  spent 2200
      dexter gave  (harry, 2000)
      angel  gave  (debra, 3200)
    })



----------

Kudos for the original poster for a nice example!
