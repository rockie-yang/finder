package finder

/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
class FileInfo(val fullPath: String, val lastModified: Long,
               val title: String, val content: String,
               val tags: List[String] = Nil) {

}
