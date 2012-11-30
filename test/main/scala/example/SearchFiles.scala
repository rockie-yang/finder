package example

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License") you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.{InputStreamReader, FileInputStream, File, BufferedReader}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.index.{IndexReader, DirectoryReader}
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.queryparser.classic.QueryParser
import java.util.Date

//import org.apache.lucene.queryparser.classic.QueryParser

import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query

/** Simple command-line based search demo. */
object SearchFiles {

  def main(args: Array[String]) = {
    /** Simple command-line based search demo. */
    val usage =
      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\n " +
        "See http://lucene.apache.org/java/4_0/demo.html for details."
    println(usage)

    def nextOption(map: Map[Symbol, String], list: List[String]): Map[Symbol, String] = {
      def isSwitch(s: String) = (s(0) == '-')
      list match {
        case Nil => map

        case "-index" :: value :: tail =>
          nextOption(map + ('indexPath -> value), tail)
        case "-field" :: value :: tail =>
          nextOption(map + ('field -> value), tail)
        case "-repeat" :: value :: tail =>
          nextOption(map + ('repeat -> value), tail)

        case "-queries" :: value :: tail =>
          nextOption(map + ('queries -> value), tail)
        case "-raw" :: tail =>
          nextOption(map + ('raw -> "true"), tail)
        case "-paging" :: tail =>
          nextOption(map + ('hitsPerPage -> "true"), tail)

        case option :: tail => {
          println("Unknown option " + option)
          sys.exit(1)
        }

      }
    }

    val defaultVal = Map(
      'indexPath -> "/tmp/lucene",
      'field -> "contents",
      'repeat -> "0",
      'raw -> "false",
      'hitsPerPage -> "10"
    )

    val options = nextOption(defaultVal, args.toList)


    val reader = DirectoryReader.open(FSDirectory.open(new File(options('indexPath))))
    val searcher = new IndexSearcher(reader)
    val analyzer = new StandardAnalyzer(Version.LUCENE_40)

    val in = if (options.contains('queries)) {
      new BufferedReader(new InputStreamReader(new FileInputStream(options('queries)), "UTF-8"))
    } else {
      new BufferedReader(new InputStreamReader(System.in, "UTF-8"))
    }
    val parser = new QueryParser(Version.LUCENE_40, options('field), analyzer)

    while (true) {

      val line = if (options.contains('queries)) options('queries)
      else {
        println("Enter query: ")
        in.readLine().trim
      }

      val query = parser.parse(line)
      println("Searching for: " + query.toString(options('field)))

      // repeat & time as benchmark
      val start = new Date()
      for (i <- 0 until options('repeat).toInt) {
        searcher.search(query, null, 100)
      }
      val end = new Date()
      println("Time: " + (end.getTime() - start.getTime()) + "ms")


      doPagingSearch(in, searcher, query, options('hitsPerPage).toInt, options('raw).toBoolean, !options.contains('queries))

    }
    reader.close()
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   *
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   *
   */
  def doPagingSearch(in: BufferedReader, searcher: IndexSearcher, query: Query,
                     hitsPerPage: Int, raw: Boolean, interactive: Boolean) {

    // Collect enough docs to show 5 pages
    val results = searcher.search(query, 5 * hitsPerPage)
    var hits = results.scoreDocs

    val numTotalHits = results.totalHits
    System.out.println(numTotalHits + " total matching documents")

    var start = 0
    var end = Math.min(numTotalHits, hitsPerPage)

    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits + " total matching documents collected.")
        System.out.println("Collect more (y/n) ?")
        val line = in.readLine()
        if (line.length() == 0 || line.charAt(0) == 'n') {
          //          // break ??
        }

        hits = searcher.search(query, numTotalHits).scoreDocs
      }

      end = Math.min(hits.length, start + hitsPerPage)

      for (i <- start until end) {
        if (raw) {
          // output raw format
          System.out.println("doc=" + hits(i).doc + " score=" + hits(i).score)
          //          continue?
        }

        val doc = searcher.doc(hits(i).doc)
        val path = doc.get("path")
        if (path != null) {
          System.out.println((i + 1) + ". " + path)
          val title = doc.get("title")
          if (title != null) {
            System.out.println("   Title: " + doc.get("title"))
          }
        } else {
          System.out.println((i + 1) + ". " + "No path for this document")
        }

      }

      if (!interactive || end == 0) {
        //        // break ?
      }

      if (numTotalHits >= end) {
        var quit = false
        while (true) {
          System.out.print("Press ")
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ")
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ")
          }
          System.out.println("(q)uit or enter number to jump to a page.")

          val line = in.readLine()
          if (line.length() == 0 || line.charAt(0) == 'q') {
            quit = true
            // break ?
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage)
            // break ?
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start += hitsPerPage
            }
            // break ?
          } else {
            val page = Integer.parseInt(line)
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage
              // break ?
            } else {
              System.out.println("No such page")
            }
          }
        }
        if (quit) // break ?
          end = Math.min(numTotalHits, start + hitsPerPage)
      }
    }
  }
}
