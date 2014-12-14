/**
 * This file has been lifted from scalaz -https://github.com/scalaz/scalaz/blob/series/7.2.x/tests/src/test/scala/scalaz/SpecLite.scala
 *
 * All original copyrights/attributions/legalStuff from the original apply
 *
 * Reasons for lift:
 *   - name is set by a macro so that it works with scalajs
 *
 * Other changes:
 *   - wrapped "in" with a try/catch. Have to make sure that real exceptions are still handled
 */

package zcheck

import scalaz._
import zcheck.macros.ClassMacros._
import reflect.ClassTag

import org.scalacheck._
import org.scalacheck.Prop.Result
import org.scalacheck.Prop.Exception
import org.scalacheck.Gen.Parameters

abstract class SpecLite extends Properties("") {

  override val name:String =  className(this)

  def checkAll(name: String, props: Properties) =
    for ((name2, prop) <- props.properties) yield {
      property(name + ":" + name2) = prop
    }


  def checkAll(props: Properties) = for ((name, prop) <- props.properties) yield  property(name) = prop

  implicit class PropertyOps(props: Properties) {
    def withProp(propName: String, prop: Prop) = new Properties(props.name) {
      for {(name, p) <- props.properties} property(name) = p
      property(propName) = prop
    }
  }

  private var context: String = ""

  def check(x: => Boolean): Prop = x must_== true

  def fail(msg: String): Nothing = throw new AssertionError(msg)

  implicit class StringOps(s: String) {
    def should[A](a: => Any): Unit = {
      val saved = context
      context = s; try a finally context = saved
    }
    def ![A](a: => A)(implicit ev: (A) => Prop): Unit = in(a)
    def in[A](a: => A)(implicit ev: (A) => Prop): Unit = property(context + ":" + s) = new Prop {
      def apply(prms: Parameters): Result = {
        try ev(a).apply(prms) catch {
          case e: Throwable => Result(status = Exception(e))
        }
      } // TODO sort out the laziness / implicit conversions properly
    }
  }

  implicit class AnyOps[A](actual: => A) {
    def must_===(expected: A)(implicit show: Show[A], equal: Equal[A]): Unit = {
      val act = actual
      def test = Equal[A].equal(expected, act)
      def koMessage = "%s !== %s".format(Show[A].shows(act), Show[A].shows(expected))
      if (!test)
        fail(koMessage)
    }
    def must_==(expected: A): Unit = {
      val act = actual
      def test = expected == act
      def koMessage = "%s !== %s".format(act, expected)
      if (!test)
        fail(koMessage)
    }

    def mustMatch(f: PartialFunction[A, Boolean]): Unit = {
      val act = actual
      def test = f.isDefinedAt(act) && f(act)
      def koMessage = "%s does not satisfy partial function".format(act)
      if (!test)
        fail(koMessage)
    }

    def and[B](b: => B): B = {
      actual
      b
    }

    def mustBe_<(x: Int)(implicit ev: A <:< Int) = {
      val act = actual
      def test = ev(act) < x
      def koMessage = "%s <! %s".format(actual, x)
      if (!test)
        fail(koMessage)
    }

    def mustThrowA[T <: Throwable](implicit man: ClassTag[T]): Unit = {
      val erasedClass = man.runtimeClass
      try {
        actual
        fail("no exception thrown, expected " + erasedClass)
      } catch {
        case ex: Throwable =>
          if (!erasedClass.isInstance(ex))
            fail("wrong exception thrown, expected: " + erasedClass + " got: " + ex)
      }
    }
  }

  def prop[T, R](result: T => R)(implicit toProp: (=>R) => Prop, a: Arbitrary[T], s: Shrink[T]): Prop = check1(result)
  implicit def propToProp(p: => Prop): Prop = p
  implicit def check1[T, R](result: T => R)(implicit toProp: (=>R) => Prop, a: Arbitrary[T], s: Shrink[T]): Prop = Prop.forAll((t: T) => toProp(result(t)))
  implicit def unitToProp(u: => Unit): Prop = booleanToProp({u; true})
  implicit def unitToProp2(u: Unit): Prop = booleanToProp(true)
  implicit def booleanToProp(b: => Boolean): Prop = Prop.secure(b)

  /**
   * Most of our scalacheck tests use (Int => Int). This generator includes non-constant
   * functions (id, inc), to have a better chance at catching bugs.
   */
  implicit def Function1IntInt[A](implicit A: Arbitrary[Int]): Arbitrary[Int => Int] =
    Arbitrary(Gen.frequency[Int => Int](
      (1, Gen.const((x: Int) => x)),
      (1, Gen.const((x: Int) => x + 1)),
      (3, A.arbitrary.map(a => (_: Int) => a))
    ))
}