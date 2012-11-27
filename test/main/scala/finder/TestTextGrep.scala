/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/26/12
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */

package finder

import java.io.File
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestTextGrep extends FunSuite {
  test("grep single text") {
    val matches = new TextGreper().grep(new File("./test/SingleText.txt"), "text");

    assert(matches === List("text"))
  }
}
