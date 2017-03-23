name := "akkaCrawler"

version := "1.0"

scalaVersion := "2.11.8"

lazy val akkaVersion = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
  ,"com.typesafe.akka" %% "akka-cluster" % akkaVersion
  ,"com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion

  // crawling lib
  ,"net.ruippeixotog" %% "scala-scraper" % "1.2.0"

  ,"org.slf4j" % "slf4j-simple" % "1.6.4"

  // deployment on AWS
  ,"com.amazonaws" % "aws-java-sdk" % "1.11.8"

  // testing
  ,"com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  ,"com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion
  ,"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

resolvers ++= Seq(
  // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)
