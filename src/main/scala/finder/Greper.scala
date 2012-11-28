package finder

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
trait Matcher {
  def apply(origin: String, find: String): Boolean
}

object ContainMatcher extends Matcher{
  def apply(origin: String, find: String): Boolean =
    origin.contains(find)
}
trait Greper {


  def grep(file: java.io.File, content: String)
}
