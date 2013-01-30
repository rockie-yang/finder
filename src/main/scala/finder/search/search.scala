package finder.search

import finder.common.{ContainStringMatcher, FileTraverser}
import java.io.File
import scala.Predef._


object search {
  val usage = """
                |Usage: search [-p filePattern] [-d searchDepth] [-l searchLines] pattern path
                |   if don't specify -p the filePattern, then try to search all files
                |   if don't specify -d the searchDepth, then search all sub folders
                |   if don't specify -f the searchLine, then search all lines
              """.stripMargin

  def main(args: Array[String]) {
    if (args.length <= 2) println(usage)
    //    else {
    val argList = args.toList

    def nextOption(map: Map[String, Any], list: List[String]): Map[String, Any] = {
      def isSwitch(s: String) = (s(0) == '-')
      list match {
        case Nil => map
        case "-p" :: filePattern :: tail =>
          nextOption(map ++ Map("filePattern" -> filePattern), tail)
        case "-d" :: searchDepth :: tail =>
          nextOption(map ++ Map("searchDepth" -> searchDepth.toInt), tail)
        case "-l" :: searchLines :: tail =>
          nextOption(map ++ Map("searchLines" -> searchLines.toInt), tail)
        case pattern :: path :: Nil =>
          nextOption(map ++ Map("pattern" -> pattern) ++ Map("path" -> path), list.tail)
        case option :: tail => println("Unknown option " + option)
        exit(1)
      }
    }


    // this is just a test for search
    val default = Map(
      "path" -> "/Users/yangyoujiang/scala/finder/src/",
      "pattern" -> "pattern",
      "filePattern" -> ".*",
      "searchDepth" -> Int.MaxValue,
      "searchLines" -> Int.MaxValue
    )
    val options = nextOption(default, argList)

    if (options.contains("pattern") && options.contains("path")) {
      grep(options)
      println("search finished")
    }
    else println(options)
    //    }
  }


  def grep(options: Map[String, Any]) {
    val filePattern = options("filePattern").toString
    val searchDepth = options("searchDepth").toString.toInt
    val searchLines = options("searchLines").toString.toInt
    val pattern = options("pattern").toString
    val path = options("path").toString

    val listener = new ResultPrintOutListener

    val matcher = new ContainStringMatcher(pattern)

    val filePredicate = (file: File) => file.getName.matches(filePattern)
    val textGreper = new TextGreper(listener, matcher, searchLines)
    val traverser = new FileTraverser(filePredicate, textGreper, searchDepth)

    traverser.traverse(path)

  }
}
