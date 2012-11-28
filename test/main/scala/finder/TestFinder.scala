package finder

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


/**
* Created with IntelliJ IDEA.
* User: yangyoujiang
* Date: 11/28/12
* Time: 3:46 PM
* To change this template use File | Settings | File Templates.
*/

@RunWith(classOf[JUnitRunner])
class TestFinder extends AkkaSuite{

  test("finder single text") {


    Finder.find(".", "text", listener, ContainMatcher)

    //    assert(matches === List("text"))
  }
}
