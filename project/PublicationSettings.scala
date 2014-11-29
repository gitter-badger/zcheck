
import bintray.Plugin._
import bintray.Keys._
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin.ReleaseKeys._

import sbtrelease.ReleasePlugin._

object PublicationSettings {

  lazy val publicationSettings = bintrayPublishSettings ++ releaseSettings ++ Seq(
    crossBuild := true,

    publishMavenStyle := false,

    publishArtifact in Test := false,
    repository in bintray := "releases",
    licenses +=("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    bintrayOrganization in bintray := None

  )
}
 