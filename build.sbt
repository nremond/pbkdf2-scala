import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "pbkdf2-scala"

organization := "io.github.nremond"

scalaVersion := "3.1.2"

crossScalaVersions := Seq("3.1.2", "2.13.8", "2.12.15")

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) =>
      Seq(
          "org.scalatest"   %% "scalatest"  % "3.2.11"   % Test,
          "org.scalacheck" %% "scalacheck" % "1.15.4" % Test
      )
    case _ =>
      Seq (
          "org.scalatest"   %% "scalatest"  % "3.2.11"   % Test,
          "org.scalacheck" %% "scalacheck" % "1.16.0" % Test
      )
  }
}


resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

/// Scalariform
scalariformAutoformat := true

scalariformPreferences := ScalariformKeys.preferences.value
      .setPreference(DoubleIndentConstructorArguments, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(IndentLocalDefs, true)

/// Publishing
publishTo := sonatypePublishToBundle.value

pomExtra in Global :=
  <url>http://github.com/nremond/pbkdf2-scala</url>
  <licenses>
    <license>
      <name>Apache</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:nremond/pbkdf2-scala.git</url>
    <connection>scm:git@github.com:nremond/pbkdf2-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>nremond</id>
      <name>Nicolas Rémond</name>
      <url>http://nremond.github.io</url>
    </developer>
  </developers>



import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(releaseStepCommand("publishSigned"), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(releaseStepCommand("sonatypeReleaseAll"), enableCrossBuild = true),
  pushChanges
)

usePgpKeyHex("39566741592AD58743524321185B009D719F2229")
