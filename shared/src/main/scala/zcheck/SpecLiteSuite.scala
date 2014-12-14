package zcheck

import org.scalacheck._

case class SpecLiteSuite(tests: Seq[Properties]) {

  def checkAll(): Seq[(String, Test.Result)] = for {
    props <- tests
    (name, prop) <- props.properties
  } yield (name, Test.check(Test.Parameters.default, prop))

}
