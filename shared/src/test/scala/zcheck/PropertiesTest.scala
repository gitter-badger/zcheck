package zcheck

import org.scalacheck._
import org.scalacheck.Prop.Result
import org.scalacheck.Gen.Parameters
import org.scalacheck.util.Pretty.{Params, pretty, format}

object PropertiesTest extends SpecLite {

  "All Tests should" should {

    "check something" in {
      check(10 > 5)
    }

  }
}

object TheTestSuiteTest extends SpecLite {
  val t = zcheck.TheTestSuite
  //println(t.run)
}

object TheTestSuite extends SpecLite {
  //Todo: This is messy - all of it.
 
  val s = new SpecLiteSuite(Seq(
    PropertiesTest,
    SpecLiteTests))

  case class SauceLabsResult(passed:Int, failed:Int) {
  }

  //@JSExport
  def run():String = {
    //Todo: This is messy - all of it.

    val verbosity = 0
     val prettyPrms = Params(verbosity)

    val r = s.checkAll()
    var tests: String ="" //"Running tests:\n"
    var passed:Int =0
    var failed:Int =0
    var duration:Long =0

    r.map(r => {
        if (r._2.passed) passed = passed + 1 else failed = failed + 1
      duration = duration + r._2.time
    })
    val total = passed + failed
    val header: String = s"""{"passed": $passed,"failed": $failed,"total": $total,"duration": $duration,"tests": ["""

    var sep =""
    r.map(r => {
      val message = pretty(r._2, prettyPrms)
      tests += s"""$sep{"name": "${r._1}","result": ${r._2.passed},"message": "$message","duration": ${r._2.time}}"""
      sep = ","
    })

    val result = header + tests + "]}"     
    result.replace("\n>", ":").replace("\n", ", ")
  }
}