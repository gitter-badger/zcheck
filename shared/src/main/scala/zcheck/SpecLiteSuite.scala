package zcheck


import org.scalacheck._
import org.scalacheck.Prop.Result
import org.scalacheck.Gen.Parameters

case class SpecLiteSuite(tests: Seq[Properties]) {

  def checkAll(): Seq[(String, Test.Result)] = {
    for {
      props <- tests
      (name, prop) <- props.properties
    } yield {
      ((name, Test.check(Test.Parameters.default, prop)))
    }
  }
}
