
import sbt._
import sbt.Keys._
import com.inthenow.sbt.scalajs._

import com.inthenow.sbt.scalajs.SbtScalajs._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object SimpleBuild extends Build {


  val logger = ConsoleLogger()

  lazy val buildSettings: Seq[Setting[_]] = Seq(
    organization := "com.github.inthenow",
    scalaVersion := "2.11.4",
    crossScalaVersions := Seq("2.11.4", "2.10.4"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    resolvers += Resolver.url("inthenow-releases",
    url("http://dl.bintray.com/inthenow/releases"))(Resolver.ivyStylePatterns)

  )

 val jsSettings = Seq(
  libraryDependencies += "com.github.inthenow" %%% "zcheck" % "0.5.1",
    ScalaJSKeys.scalaJSTestFramework := "org.scalacheck.ScalaCheckFramework"
  )

  val jvmSettings = Seq(
    libraryDependencies += "com.github.inthenow" %% "zcheck" % "0.5.1",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-maxSize", "5", "-minSuccessfulTests", "33", "-workers", "1", "-verbosity", "1")
  )

  implicit val js: JsTarget = new JsTarget(settings = JsTarget.js.settings ++ jsSettings)
  implicit val jvm: JvmTarget = new JvmTarget(settings = JvmTarget.jvm.settings ++ jvmSettings)

  val module = XModule(id = "simple", defaultSettings = buildSettings)

  lazy val rdf       = module.project(prjJvm, prjJs)
  lazy val prjJvm    = module.jvmProject(sharedjvm)
  lazy val prjJs     = module.jsProject(sharedjs)
  lazy val sharedjvm = module.jvmShared()
  lazy val sharedjs  = module.jsShared(sharedjvm)
}
