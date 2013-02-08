package finder.indexer

import finder.common.{SleepProcessor, FileProcessors, FileTraverser}
import java.io.File

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/31/13
 */
object Indexer {


  def main(args: Array[String]) {
    // Index should be in low priority to get better user responsiveness

    Thread.currentThread().setPriority(Thread.MIN_PRIORITY)
    val options = parseArgs(args)
    work(options)
  }

  def work(options: Map[String, Any]) {
    val indexPath = new File(options("indexPath").toString)
    indexPath.mkdirs()

    val searchDepth = options("indexDepth").toString.toInt
    val fileIndexer = new FileIndexer(indexPath.getAbsolutePath)
    // for each file index, just sleep 1 milli second, to give a way cpu control
    val processors = new FileProcessors(fileIndexer, new SleepProcessor)
    val filePredicate = (file: File) => true
    val path = options("path").toString

    val traverser = new FileTraverser(filePredicate, processors, searchDepth)

    traverser.traverse(path)
  }

  def parseArgs(args: Array[String]): Map[String, Any] = {
    val usage = """
                  |Usage: indexer [-i indexPath] [-d indexDepth] path
                  |   if don't specify -i the indexPath, then use HOME/.index to store indexes
                  |   if don't specify -d the searchDepth, then search all sub folders
                """.stripMargin


    if (args.length <= 2) println(usage)
    //    else {
    val argList = args.toList

    def nextOption(map: Map[String, Any], list: List[String]): Map[String, Any] = {
      def isSwitch(s: String) = (s(0) == '-')
      list match {
        case Nil => map
        case "-i" :: indexPath :: tail =>
          nextOption(map ++ Map("indexPath" -> indexPath), tail)
        case "-d" :: indexDepth :: tail =>
          nextOption(map ++ Map("indexDepth" -> indexDepth), tail)
        case path :: Nil =>
          nextOption(map ++ Map("path" -> path), list.tail)
        case option :: tail => println("Unknown option " + option)
        exit(1)
      }
    }

    val userHome = System.getProperty("user.home")
    // this is just a test for search
    val default = Map(
      "path" -> userHome,
      "indexPath" -> (userHome + "/.index"),
      "indexDepth" -> 2
    )
    val options = nextOption(default, argList)

    println(userHome)

    options
  }
}
