package finder

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
object Config {
  val defaultConfig = Map("indexPath" -> "/tmp")

  def apply(name: String): String = {
    defaultConfig(name)
  }
}
