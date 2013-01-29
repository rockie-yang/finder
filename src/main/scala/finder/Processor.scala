package finder

import java.io.File

/**
 * Created by : Rockie Yang(eyouyan@gmail.com, snowriver.org)
 * Created at : 1/26/13
 */
trait Processor {

  def apply(file: File)
}

class Processors(processors: Processor*) extends Processor{
  def apply(file: File) {
    processors.foreach(processor => processor(file))
  }
}
