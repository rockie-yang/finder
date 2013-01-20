package finder

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
@RunWith(classOf[JUnitRunner])
class TestFinder extends AkkaSuite {

  test("finder single text") {


    Finder.find(".", "text", listener, ContainMatcher)

    //    assert(matches === List("text"))
  }
}
