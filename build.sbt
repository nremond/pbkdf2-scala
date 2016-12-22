import scalariform.formatter.preferences._

name := "pbkdf2-scala"

organization := "io.github.nremond"

version := "0.6"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.12.1", "2.11.8", "2.10.6")

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"  % "3.0.1"   % Test,
  "org.scalacheck"  %% "scalacheck" % "1.13.4"  % Test
)

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

/// Scalariform
scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(DoubleIndentClassDeclaration, true)
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
