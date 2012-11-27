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
import akka.actor.{Props, ActorSystem}


@RunWith(classOf[JUnitRunner])
class TestTextGreper extends AkkaSuite {
  test("grep single text") {

    val file = new File(this.getClass.getResource("SingleText.txt").getFile)
    val matches = new TextGreper(listener).grep(file, "text");

//    assert(matches === List("text"))
  }

  test("using factory") {
    println("test")
    val file = new File(this.getClass.getResource("SingleText.txt").getFile)
    grep(file, "text", listener)
//    assert(matches === List("text"))

    grep(file, "notext", listener)
//    assert(nomatch === List())
  }
}
