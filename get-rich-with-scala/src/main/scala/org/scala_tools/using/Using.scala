package org.scala_tools.using

trait Using {

  // from slide 21 of Martin Odersky's Scala Talk at FOSDEM 2009
  // http://www.slideshare.net/Odersky/fosdem-2009-1013261
  def using[T <: { def close() }]
      (resource: T)
      (block: T => Unit) {
    try{
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }
  
}
