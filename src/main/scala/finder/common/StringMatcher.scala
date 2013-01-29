package finder.common

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
trait StringMatcher {
  def apply(origin: String): Boolean
}


class ContainStringMatcher(matchStr: String) extends StringMatcher {
  def apply(content: String): Boolean =
    content.contains(matchStr)
}
