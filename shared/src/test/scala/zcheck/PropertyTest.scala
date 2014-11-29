package zcheck

import org.scalacheck._

object PropertyTest extends Properties("PropertyTest") {

  property("dummy") = Prop.forAll { l: List[String] => l.reverse.reverse == l}

}
