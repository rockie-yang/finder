package finder

import java.io.{FileInputStream, File}
import org.apache.tika.metadata.Metadata
import org.apache.tika.config.TikaConfig
import org.apache.tika.parser.{Parser, ParseContext, AutoDetectParser}
import org.apache.tika.sax.BodyContentHandler
import org.apache.lucene.document.{StringField, TextField, Field, Document}
import org.apache.lucene.document.Field.Store
import org.apache.tika.Tika
import org.xml.sax.helpers.DefaultHandler


/**
 * Created with IntelliJ IDEA.
 * User: yangyoujiang
 * Date: 12/13/12
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
object Indexer extends App{
  val DEBUG: Boolean = false;                     //1
  val textualMetadataFields = Set(Metadata.TITLE, Metadata.DESCRIPTION, Metadata.SUBJECT);     //2

  val config = TikaConfig.getDefaultConfig(); //3

  val tika = new Tika(config)



  val file = new File("./pom.xml")
  val metadata = new Metadata();
  val stream = new FileInputStream(file);
  val content = tika.parseToString(stream, metadata)

  val ftype = tika.detect(file)

  println("file type is : " + ftype)

  val content2 = tika.parseToString(file)
  println("metadata: " + metadata.toString)
  println("content : " + content)
  println("content2: " + content2)

  val parser = tika.getParser();
  val handler = new DefaultHandler();
  parser.parse(
    new FileInputStream(file), handler, metadata, new ParseContext());


  if (file.exists()) println("parse the file" + file.getAbsolutePath)
  val i = index(file)

  def index(file: File): Boolean = {
    val metadata = new Metadata();
//    metadata.set(Metadata.IDENTIFIER, file.getAbsolutePath);   // 4
    // If you know content type (eg because this document
    // was loaded from an HTTP server), then you should also
    // set Metadata.CONTENT_TYPE
    // If you know content encoding (eg because this
    // document was loaded from an HTTP server), then you
    // should also set Metadata.CONTENT_ENCODING

    val is = new FileInputStream(file);      // 5
    val parser = new AutoDetectParser();       // 6
    val handler = new BodyContentHandler(); // 7
    val context = new ParseContext();

//    context.set(parser., parser);
    try {
      parser.parse(is, handler, metadata, new ParseContext());
      println(handler.toString)
    } finally {
      is.close();
    }
    val doc = new Document();

    doc.add(new TextField("contents", handler.toString, Store.NO));

    doc.add(new StringField("fileName", file.getAbsolutePath, Store.YES));

    println(metadata.names.mkString(","))
    true
  }
}
