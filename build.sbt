name := "pbkdf2-scala"

version := "0.1"                                                       

organization := "nremond"                                        

scalaVersion := "2.9.2"   


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "Maven2 Repository" at "http://repo2.maven.org/maven2/"

resolvers += "Apache Maven2 Repository" at "http://repo1.maven.org/maven2/"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.9.0" % "1.8" % "test"
)   