import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val scalaz     = "com.googlecode.scalaz" % "scalaz-core_2.8.0" % "5.0"

  override def compileOptions = super.compileOptions ++ compileOptions("-unchecked")
}
