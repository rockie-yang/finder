package finder

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 12/13/12
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
trait Indexer {
  def index(file: File): Boolean
}
