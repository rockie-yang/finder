package finder

import org.apache.lucene.index.{IndexReader, IndexWriterConfig, Term, IndexWriter}
import java.io.{InputStreamReader, BufferedReader, FileInputStream, File}
import org.apache.lucene.document._
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version

/**
 * User: Rockie Yang
 * Date: 12/2/12
 * Time: 9:35 PM
 */
class TextIndexer(val indexPath: String)  {

  val dir = FSDirectory.open(new File(indexPath))
  val analyzer = new StandardAnalyzer(Version.LUCENE_40)
  val iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer)
  // Create a new index in the directory, removing any
  // previously indexed documents:
  iwc.setOpenMode(OpenMode.CREATE)


  /**
   * Indexes the given file using the given writer, or if a val is given,
   * recurses over files and directories found under the given directory.
   *
   * NOTE: This method indexes one document per input file.  This is slow.  For good
   * throughput, put multiple documents into your input file(s).  An example of this is
   * in the benchmark module, which can create "line doc" files, one document per line,
   * using the
   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
   * >WriteLineDocTask</a>.
   *
   * @param writer Writer to the index where the given file/dir info will be stored
   * @param file The file to index, or the val to recurse into to find files to index
   * @throws java.io.IOException If there is a low-level I/O error
   */
  def indexDocs(writer: IndexWriter, file: File) {
    require(file.isFile)

    // do not try to index files that cannot be read
    if (file.canRead) {
      val fis: FileInputStream = new FileInputStream(file)

      try {

        // make a new, empty document
        val doc = new Document()

        // Add the path of the file as a field named "path".  Use a
        // field that is indexed (i.e. searchable), and tokenize
        // the field into separate words and don't index term frequency
        // or positional information:
        val pathField = new TextField("path", file.getAbsolutePath, Field.Store.YES)
        doc.add(pathField)

        // Add the last modified val  of the file a field named "modified".
        // Use a Longval that is indexed (i.e. efficiently filterable with
        // NumericRangeFilter).  This indexes to milli-second resolution, which
        // is often too fine.  You could instead create a number based on
        // year/month/day/hour/minutes/seconds, down the resolution you require.
        // For example the long value 2011021714 would mean
        // February 17, 2011, 2-3 PM.
        doc.add(new LongField("modified", file.lastModified, Field.Store.NO))

        // Add the contents of the file to a field named "contents".  Specify a Reader,
        // so that the text of the file is tokenized and indexed, but not stored.
        // Note that FileReader expects the file to be in UTF-8 encoding.
        // If that's not the case searching for special characters will fail.
        doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))))

        if (writer.getConfig.getOpenMode == OpenMode.CREATE) {
          // New index, so we just add the document (no old document can be there):
          println("adding " + file)
          writer.addDocument(doc)
        } else {
          // Existing index (an old copy of this document may have been indexed) so
          // we use upval val instead to replace the old one matching the exact
          // path, if present:
          println("updating " + file)
          writer.updateDocument(new Term("path", file.getPath), doc)
        }

      } finally {
        fis.close()
      }
    }
    else {
      // The file can not be read
      // TODO it need be replaced as more general solution
      println("The file can not be read")
    }

  }

  def index(file: File): Boolean = false
}
