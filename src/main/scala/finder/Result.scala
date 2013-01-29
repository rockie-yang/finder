package finder

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/28/13
 */
case class Result(fileName: String, matches: List[String])    {
  override def toString = fileName + " : " + matches.mkString("\n")
}

trait ResultProcessor {
  def apply(result: Result)
}

class ResultPrintOut extends ResultProcessor {
  def apply(result: Result) {
    println(result.toString)
  }
}
