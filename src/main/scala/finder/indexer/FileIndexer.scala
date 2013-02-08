package finder.indexer

import finder.common.FileProcessor
import java.io.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.{Term, IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version


/**
 * Created by :  Rockie Yang (eyouyan@gmail.com, snowriver.org)
 * Created at :  1/20/13
 */
class FileIndexer(indexPath: String) extends FileProcessor {
  val dir = FSDirectory.open(new File(indexPath))
  val analyzer = new StandardAnalyzer(Version.LUCENE_40)
  val iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer)
  // Create a new index in the directory, removing any
  // previously indexed documents:
  iwc.setOpenMode(OpenMode.CREATE)


  def apply(file: File) {
    val writer = new IndexWriter(dir, iwc)
    val document = DocumentFactory.getDocument(file)
    if (writer.getConfig.getOpenMode == OpenMode.CREATE) {
      // New index, so we just add the document (no old document can be there):
      println("adding " + file)
      writer.addDocument(document)
    } else {
      // Existing index (an old copy of this document may have been indexed) so
      // we use upval val instead to replace the old one matching the exact
      // path, if present:
      println("updating " + file)
      writer.updateDocument(new Term("path", file.getPath), document)
    }
    writer.close()
  }

  //  val DEBUG: Boolean = false
  //  //1
  //  val textualMetadataFields = Set(Metadata.TITLE, Metadata.DESCRIPTION, Metadata.SUBJECT)
  //  //2
  //
  //  val config = TikaConfig.getDefaultConfig
  //
  //  val tika = new Tika(config)
  //
  //
  //  val file = new File(".project")
  //
  //  val metadata = new Metadata()
  //
  //  val stream = new FileInputStream(file);
  //  val content = tika.parseToString(stream, metadata).trim
  //
  //  val ftype = tika.detect(file)
  //
  //  println("file type is : " + ftype)
  //
  //  val content2 = tika.parseToString(file)
  //  println("metadata: " + metadata.toString)
  //  println("content : " + content)
  //  println("content2: " + content2)
  //
  //  val parser = tika.getParser
  //  val handler = new DefaultHandler
  //  parser.parse(
  //    new FileInputStream(file), handler, metadata, new ParseContext)
  //
  //
  //  if (file.exists()) println("parse the file" + file.getAbsolutePath)
  //  val i = index(file)
  //
  //  def index(file: File): Boolean = {
  //    val metadata = new Metadata()
  //    //    metadata.set(Metadata.IDENTIFIER, file.getAbsolutePath);   // 4
  //    // If you know content type (eg because this document
  //    // was loaded from an HTTP server), then you should also
  //    // set Metadata.CONTENT_TYPE
  //    // If you know content encoding (eg because this
  //    // document was loaded from an HTTP server), then you
  //    // should also set Metadata.CONTENT_ENCODING
  //
  //    val is = new FileInputStream(file) // 5
  //    val parser = new AutoDetectParser() // 6
  //    val handler = new BodyContentHandler() // 7
  //    val context = new ParseContext
  //
  //    //    context.set(parser., parser);
  //    try {
  //      parser.parse(is, handler, metadata, new ParseContext)
  //      println(handler.toString)
  //    } finally {
  //      is.close()
  //    }
  //    val doc = new Document
  //
  //
  //    doc.add(new TextField("contents", handler.toString, Store.NO))
  //    doc.add(new StringField("fileName", file.getAbsolutePath, Store.YES))
  //
  //    println(metadata.names.mkString(","))
  //    true
  //  }
}
