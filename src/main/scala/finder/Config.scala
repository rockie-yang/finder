package finder

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 12/6/12
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
object Config {
  val defaultConfig = Map("indexPath" -> "/tmp")
  def apply(name: String): String = {
    defaultConfig(name)
  }
}
