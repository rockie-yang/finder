package finder.grep

import finder.common.ContainStringMatcher
import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */

@RunWith(classOf[JUnitRunner])
class TestTextGreper extends FunSuite {
  test("using factory") {
    println("test")


    val file = new File(this.getClass.getResource("SingleText.txt").getFile)

    val existListener = new ResultHolderListener
    new TextGreper(existListener, new ContainStringMatcher("text"))(file)
    println(existListener)
    assert(existListener.results === List(Result(file, List("text"))))

    val noneExistListener = new ResultHolderListener
    new TextGreper(noneExistListener, new ContainStringMatcher("notext"))(file)
    assert(noneExistListener.results === List[Result]())
  }
}
