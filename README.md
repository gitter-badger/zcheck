zCheck
=====

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/InTheNow/zcheck?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

zCheck is a small testing library that works for both Scala/ScalaJS and is based on ScalaCheck.

To get started add the following to your sbt settings
For ScalaJS projects:

```scala
libraryDependencies += "com.github.inthenow" %%% "zcheck" % "0.5.3"

libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.12.1" % "test"

ScalaJSKeys.scalaJSTestFramework := "org.scalacheck.ScalaCheckFramework"
```
For ScalaJVM projects:
```scala
libraryDependencies += "com.github.inthenow" %% "zcheck" % "0.5.3"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.1" % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck)
```

Core feature of zCheck is syntax sugar that enrich scalacheck with `"should"`, `"must"`, `"in"` and other common test matchers.
To start writing yor tests just extends `SpecLite` class and write your tests there.
Here is a simple example with core matchers.

```scala
import zcheck.SpecLite

object SpecLiteTests extends SpecLite {

  "Tests should" should {

    "check assertions" in {
      check(10 > 5)
    }

    "check exceptions" in {
      try {
        throw new Error
        fail("should have thrown")
      } catch {
        case _: Throwable => // ok
      }
    }

    "be 0 for an empty map" in {
      Seq().size must_== 0
      Seq().size mustBe_< 1
    }

    "check exceptions are thrown" in {
      //enrichAny(this)
      err(-1).mustThrowA[Error]

      //{err} .mustThrowA[Error]
    }

    "fail if wrong exceptions are thrown" in {
      try {
        err(-1).mustThrowA[RuntimeException]
        fail("should have thrown")
      }
      catch {
        case _: Throwable => // ok
      }
    }
  }

  def err(i: Int) =  if (i < 0) throw new Error else i

}
```