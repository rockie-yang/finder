package finder

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
trait Greper {
  def grep(file: java.io.File, content: String): List[String]
}
