
import org.scalacheck._
import org.scalacheck.Prop.Result
import org.scalacheck.Gen.Parameters
import zcheck._
import scala.scalajs.js.annotation.{JSExport, JSExportDescendentObjects}

@JSExport
object TheTestSuiteTestA   {
  @JSExport
  def run():String = {
     TheTestSuite.run()
  }
}
