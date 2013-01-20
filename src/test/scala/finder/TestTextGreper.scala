


package finder

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */

@RunWith(classOf[JUnitRunner])
class TestTextGreper extends AkkaSuite {
  test("grep single text") {

    val file = new File(this.getClass.getResource("SingleText.txt").getFile)
    val matches = new TextGreper(listener, ContainMatcher).grep(file, "text");

    //    assert(matches === List("text"))
  }

  test("using factory") {
    println("test")
    val file = new File(this.getClass.getResource("SingleText.txt").getFile)
    grep(file, "text", listener, ContainMatcher)
    //    assert(matches === List("text"))

    grep(file, "notext", listener, ContainMatcher)
    //    assert(nomatch === List())
  }
}
