import sbt._
import sbt.Keys._
import com.inthenow.sbt.scalajs._
import com.inthenow.sbt.scalajs.SbtScalajs._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object ZCheckBuild extends Build{
  import PublicationSettings._

  val logger = ConsoleLogger()


  lazy val buildSettings: Seq[Setting[_]] = Seq(
    organization := "com.github.inthenow",
    scalaVersion := "2.11.4",
    crossScalaVersions := Seq("2.11.4", "2.10.4"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

  val jsSettings = Seq(
    libraryDependencies += "com.github.japgolly.fork.scalaz" %%% "scalaz-core" % "7.1.0-4",
    libraryDependencies += "org.scalacheck" %%% "scalacheck" % "1.12.2-SNAPSHOT", // % "test",
    ScalaJSKeys.scalaJSTestFramework := "org.scalacheck.ScalaCheckFramework"
  )

  val jvmSettings = Seq(
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.2-SNAPSHOT", //% "test",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-maxSize", "5", "-minSuccessfulTests", "33", "-workers", "1", "-verbosity", "1")
  )

  implicit val js: JsTarget = new JsTarget(settings = JsTarget.js.settings ++ jsSettings)
  implicit val jvm: JvmTarget = new JvmTarget(settings = JvmTarget.jvm.settings ++ jvmSettings)


  // zCheck Module
  val zcheckModule = XModule(id = "zcheck",
                             defaultSettings = publicationSettings ++ buildSettings )


  lazy val zcheck            = zcheckModule.project(zcheckJvm, zcheckJs).aggregate(zcore)
  lazy val zcheckJvm         = zcheckModule.jvmProject(zcheckSharedJvm).dependsOn(zcoreJvm)
  lazy val zcheckJs          = zcheckModule.jsProject(zcheckSharedJs).dependsOn(zcoreJs)
  lazy val zcheckSharedJvm   = zcheckModule.jvmShared().dependsOn(zcoreJvm)
  lazy val zcheckSharedJs    = zcheckModule.jsShared(zcheckSharedJvm).dependsOn(zcoreJs)

 // zCheck Core Module
 val coreModule = XModule(id = "zcheckcore",
                          baseDir = "modules/zcore",
                          defaultSettings = publicationSettings ++ buildSettings ++ XScalaSettings)

 lazy val zcore            = coreModule.project(zcoreJvm, zcoreJs)
 lazy val zcoreJvm         = coreModule.jvmProject(zcoreSharedJvm)
 lazy val zcoreJs          = coreModule.jsProject(zcoreSharedJs)
 lazy val zcoreSharedJvm   = coreModule.jvmShared()
 lazy val zcoreSharedJs    = coreModule.jsShared(zcoreSharedJvm)

}
