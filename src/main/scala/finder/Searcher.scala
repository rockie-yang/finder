package finder

import java.io.{FileInputStream, InputStreamReader, File, BufferedReader}
import org.apache.lucene.search.{Query, IndexSearcher}
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.queryparser.classic.QueryParser

/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 12/6/12
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
class Searcher {

  val reader = DirectoryReader.open(FSDirectory.open(new File(Config("indexPath"))))
  val searcher = new IndexSearcher(reader)
  val analyzer = new StandardAnalyzer(Version.LUCENE_40)

  val parser = new QueryParser(Version.LUCENE_40, "Content", analyzer)

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

      end = scala.math.min(hits.length, start + hitsPerPage)

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
