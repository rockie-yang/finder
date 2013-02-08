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

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Date
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.LongField
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.Term
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version


object IndexFiles {
  def main(args: Array[String]) = {

    /** Index all text files under a directory. */
    val usage = "java org.apache.lucene.demo.IndexFiles" + " [-index INDEX_PATH] [-docs DOCS_PATH] [-create ]\n\n" +
      "This indexes the documents in DOCS_PATH, creating a Lucene index in INDEX_PATH that can be searched with SearchFiles"
    println(usage)

    def nextOption(map: Map[Symbol, String], list: List[String]): Map[Symbol, String] = {
      def isSwitch(s: String) = (s(0) == '-')
      list match {
        case Nil => map

        case "-index" :: value :: tail =>
          nextOption(map + ('indexPath -> value), tail)
        case "-docs" :: value :: tail =>
          nextOption(map + ('docsPath -> value), tail)
        case "-create" :: value :: tail =>
          nextOption(map + ('create -> value), list.tail)

        case option :: tail => {
          println("Unknown option " + option)
          sys.exit(1)
        }

      }
    }

    val defaultVal = Map('indexPath -> "/tmp/lucene", 'docsPath -> ".", 'create -> "false")

    val options = nextOption(defaultVal, args.toList)




    val docDir = new File(options('docsPath).toString)
    if (!docDir.exists() || !docDir.canRead()) {
      println("val val '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path")
      sys.exit(1)
    }

    val create = options('create).toBoolean
    val indexPath = options('indexPath)
    val start = new Date()
    try {
      println("Indexing to val '" + indexPath + "'...")

      val dir = FSDirectory.open(new File(indexPath))
      val analyzer = new StandardAnalyzer(Version.LUCENE_40)
      val iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer)

      if (create) {
        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(OpenMode.CREATE)
      } else {
        // Add new documents to an existing index:
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND)
      }

      // Optional: for better indexing performance, if you
      // are indexing many documents, increase the RAM
      // buffer.  But if you do this, increase the max heap
      // size to the JVM (eg add -Xmx512m or -Xmx1g):
      //
      // iwc.setRAMBufferSizeMB(256.0)

      val writer = new IndexWriter(dir, iwc)
      indexDocs(writer, docDir)

      // NOTE: if you want to maximize search performance,
      // you can optionally call forceMerge here.  This can be
      // a terribly costly operation, so generally it's only
      // worth it when your index is relatively (ie
      // you're done adding documents to it):
      //
      // writer.forceMerge(1)

      writer.close()

      val end = new Date()
      println(end.getTime() - start.getTime() + " total milliseconds")

    } catch {
      case e: IOException =>
        println(" caught a " + e.getClass() +
          "\n with message: " + e.getMessage())
    }
  }


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
    // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        val files = file.list()
        // an IO error could occur
        if (files != null) {
          for (i <- 0 until files.length) {
            indexDocs(writer, new File(file, files(i)))
          }
        }
      } else {

        var fis: FileInputStream = null
        try {
          fis = new FileInputStream(file)
        } catch {
          case _ => println("?")
        }

        try {

          // make a new, empty document
          val doc = new Document()

          // Add the path of the file as a field named "path".  Use a
          // field that is indexed (i.e. searchable), but don't tokenize 
          // the field into separate words and don't index term frequency
          // or positional information:
          val pathField = new StringField("path", file.getPath(), Field.Store.YES)
          doc.add(pathField)

          // Add the last modified val  of the file a field named "modified".
          // Use a Longval that is indexed (i.e. efficiently filterable with
          // NumericRangeFilter).  This indexes to milli-second resolution, which
          // is often too fine.  You could instead create a number based on
          // year/month/day/hour/minutes/seconds, down the resolution you require.
          // For example the long value 2011021714 would mean
          // February 17, 2011, 2-3 PM.
          doc.add(new LongField("modified", file.lastModified(), Field.Store.NO))

          // Add the contents of the file to a field named "contents".  Specify a Reader,
          // so that the text of the file is tokenized and indexed, but not stored.
          // Note that FileReader expects the file to be in UTF-8 encoding.
          // If that's not the case searching for special characters will fail.
          doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))))

          if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            println("adding " + file)
            writer.addDocument(doc)
          } else {
            // Existing index (an old copy of this document may have been indexed) so 
            // we use upval val instead to replace the old one matching the exact 
            // path, if present:
            println("updating " + file)
            writer.updateDocument(new Term("path", file.getPath()), doc)
          }

        } finally {
          fis.close()
        }
      }
    }
  }
}
