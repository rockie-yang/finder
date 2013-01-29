package finder

import java.io.{FileInputStream, File}
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{ParseContext, AutoDetectParser}
import org.apache.tika.sax.BodyContentHandler
import org.apache.lucene.document.{LongField, StringField, TextField, Document}
import org.apache.lucene.document.Field.Store

/**
 * It is a class create Lucene Document for a file. The content is parsed by Tika
 *
 * Exception will throw out if the file can not be parsed by tika correctly.
 *
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/20/13
 */
class Content(file: File) {
  private val metadata = new Metadata
  private val inputStream = new FileInputStream(file)
  private val handler = new BodyContentHandler

  try {
    val parser = new AutoDetectParser
    val context = new ParseContext
    parser.parse(inputStream, handler, metadata, context)
  }
  finally {
    inputStream.close()
  }

  val document = new Document
  document.add(new TextField("content", handler.toString, Store.NO))
  document.add(new StringField("fileName", file.getName, Store.YES))
  document.add(new StringField("fullFileName", file.getAbsolutePath, Store.YES))
  document.add(new LongField("lastModified", file.lastModified, Store.YES))
}
