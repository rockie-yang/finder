package finder.search

import finder.common._
import java.io.File
import scala.Predef._


object search {
  def main(args: Array[String]) {
    run(args)
  }

  // this is just a test for search
  val default = Map(
    "verbose" -> "true",
    "filePattern" -> ".*",
    "searchDepth" -> Int.MaxValue,
    "searchLines" -> Int.MaxValue,
    "ignoreCase" -> "false",
    "debug" -> "true"
  )

  val usage = """
                |Usage: search [-p filePattern] [-d searchDepth] [-l searchLines] [-v invertMatchStr] [-i]
                |  [--verbose]
                |  path pattern [pattern ...]
                |   if don't specify -p the filePattern, then try to search all files
                |   if don't specify -d the searchDepth, then search all sub folders
                |   if don't specify -f the searchLine, then search all lines
                |   if don't specify -i, match with case, otherwice ignore case
              """.stripMargin

  def run(args: Array[String]): List[Result] = {
    val options = parseArgs(args)

    val result = grep(options)

    println("search finished")

    result
  }


  def parseArgs(args: Array[String]): Map[String, Any] = {
    require(args.length >= 2, usage)

    def nextOption(map: Map[String, Any], list: List[String]): (Map[String, Any], List[String]) =
      list match {
        case "-p" :: filePattern :: tail =>
          nextOption(map ++ Map("filePattern" -> filePattern), tail)
        case "-d" :: searchDepth :: tail =>
          nextOption(map ++ Map("searchDepth" -> searchDepth.toInt), tail)
        case "-l" :: searchLines :: tail =>
          nextOption(map ++ Map("searchLines" -> searchLines.toInt), tail)
        case "--verbose" :: tail =>
          nextOption(map ++ Map("verbose" -> "true"), tail)
        case "-i" :: tail =>
          nextOption(map ++ Map("ignoreCase" -> "true"), tail)
        case "-v" :: invertMatch :: tail =>
          nextOption(map ++ Map("invertMatch" -> invertMatch), tail)
        case others => (map, others)
      }


    var (options, left) = nextOption(default, args.toList)

    require(left.length >= 2, usage)

    options += ("path" -> left.head)
    options += ("pattern" -> left.tail)

    options
  }

  def grep(options: Map[String, Any]): List[Result] = {
    val filePattern = options("filePattern").toString
    val searchDepth = options("searchDepth").toString.toInt
    val searchLines = options("searchLines").toString.toInt
    val pattern = options("pattern").toString
    val path = options("path").toString
    val ignoreCase = options("ignoreCase").toString.toBoolean
    val verbose = options("verbose").toString.toBoolean

    val debug = options("debug").toString.toBoolean
    val listener = new ResultPrintOutListener(debug)

    val includes = StringMatcher("contains", ignoreCase, List(pattern))
    val excludes = StringUnMatcher("contains", ignoreCase, List(pattern))

    val matchers = new StringMatchers(includes, excludes)

    val filePredicate = (file: File) => file.getName.matches(filePattern)
    val textGreper = new TextGreper(listener, matchers, searchLines)
    val traverser = new FileTraverser(filePredicate, textGreper, searchDepth, verbose)

    traverser.traverse(path)

    listener.results
  }
}
