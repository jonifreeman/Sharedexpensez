import scalaz._
import Scalaz._

/**
 * Original Haskell version:
 * http://lpenz.github.com/articles/hedsl-sharedexpenses/index.html
 */
object sharedexpensez {
  // Example

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

  println(solve(trip).mkString("\n"))

  // Implementation

  case class Person(name: String) {
    def spent(money: Int)(implicit s: State[Map[Person, Int], Unit]) = 
      s.withs { adjust(this, money, _) }

    def gave(borrower: Person, money: Int)(implicit s: State[Map[Person, Int], Unit]) = 
      s.withs { m => adjust(this, money, adjust(borrower, -money, m)) }
  }

  def sharedexpenses(f: State[Map[Person, Int], Unit] => State[Map[Person, Int], Unit]) = 
    f(init[Map[Person, Int]].map(_ => ())).apply(Map.empty)._1

  def solve(state: Map[Person, Int]) = {
    def solve1(err: Int, s: Map[Person, Int]): List[String] = {
      if (s.isEmpty) Nil
      else {
        val ordByMoney = Ordering[Int].on[(Person, Int)](_._2).reverse
        val (payer, debt)      = s.max(ordByMoney)
        val (receiver, credit) = s.min(ordByMoney)
        val amount = -debt min credit
        val newState = adjust(receiver, -amount, adjust(payer, amount, s))
          .filter { case (k, c) => c < -err || err < c }
        (payer.name + " pays " + amount + " to " + receiver.name) :: solve1(err, newState)
      }
    }

    val avg = (state.values.sum.toDouble / state.size).toInt
    solve1(1 + state.size, state.mapValues(_ - avg))
  }

  private def adjust(p: Person, amount: Int, m: Map[Person, Int]) =
    m.updated(p, m.getOrElse(p, 0) + amount)
}
