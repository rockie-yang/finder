package finder.indexer

import java.io.{FileInputStream, File}
import org.apache.lucene.document.Field.Store
import org.apache.lucene.document.{LongField, StringField, TextField, Document}
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{ParseContext, AutoDetectParser}
import org.apache.tika.sax.BodyContentHandler

/**
 * It is a class create Lucene Document for a file. The content is parsed by Tika
 *
 * Exception will throw out if the file can not be parsed by tika correctly.
 *
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/20/13
 */
object DocumentFactory {

  def getDocument(file: File): Document = {
    val inputStream = new FileInputStream(file)
    val document = new Document
    try {
      val parser = new AutoDetectParser
      val context = new ParseContext
      val metadata = new Metadata
      val handler = new BodyContentHandler

      parser.parse(inputStream, handler, metadata, context)

      document.add(new TextField("content", handler.toString, Store.NO))
      document.add(new StringField("fileName", file.getName, Store.YES))
      document.add(new StringField("fullFileName", file.getAbsolutePath, Store.YES))
      document.add(new LongField("lastModified", file.lastModified, Store.YES))
    }
    finally {
      inputStream.close()
    }

    document
  }


}
