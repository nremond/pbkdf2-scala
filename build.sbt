import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "pbkdf2-scala"

organization := "io.github.nremond"

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.13.1", "2.12.10", "2.11.12")

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"  % "3.0.8"   % Test,
  "org.scalacheck"  %% "scalacheck" % "1.14.1"  % Test
)

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

/// Scalariform
scalariformAutoformat := true

scalariformPreferences := ScalariformKeys.preferences.value
      .setPreference(DoubleIndentConstructorArguments, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(IndentLocalDefs, true)

/// Publishing
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
