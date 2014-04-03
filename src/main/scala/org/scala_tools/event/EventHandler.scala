package org.scala_tools.event

/* 
 * Assumed to be in the public domain: http://blog.omega-prime.co.uk/?p=21
 * @author is Max Bolingbroke, a PhD student at the University of Cambridge Computer Lab. 
 */
// TODO name clash with Event
class EventHandler[T] {
  private var invocationList : List[T => Unit] = Nil

  def apply(args : T) {
    for (invoker <- invocationList) {
      invoker(args)
    }
  }

  def +=(invoker : T => Unit) {
    invocationList = invoker :: invocationList
  }

  def -=(invoker : T => Unit) {
    invocationList = invocationList filter ((x : T => Unit) => (x != invoker))
  }

}
