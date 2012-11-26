/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 11/26/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
object GrepFactory {
   def getGrep(fileName: String): Grep = {
     if (fileName.endsWith(".txt")) new TextGrep
     else new TextGrep
   }
}
