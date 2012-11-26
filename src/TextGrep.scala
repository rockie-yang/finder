import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/25/12
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
class TextGrep extends Grep {
  def grep(file: File, content: String) = {
    val lines = scala.io.Source.fromFile(file).getLines().toList
    lines.filter(line => line.trim().matches(content))
  }
}
