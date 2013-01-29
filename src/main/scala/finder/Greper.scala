package finder

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
trait Matcher {
  def apply(origin: String): Boolean
}

class ContainMatcher(matchStr: String) extends Matcher {
  def apply(content: String): Boolean =
    content.contains(matchStr)
}

trait Greper {


  def grep(file: java.io.File)
}
