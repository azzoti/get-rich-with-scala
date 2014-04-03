package sandpit.org.scala_tools.option.math


// Modelled on Martin Odersky pimp my library technique 
// http://www.artima.com/weblogs/viewpost.jsp?thread=179766
// and implicit Numeric 
// see http://stackoverflow.com/questions/1252915/scala-how-to-define-generic-function-parameters
import Numeric._


class RichOption[T ](od:Option[T])(implicit numeric: Numeric[T])
extends Proxy 
with Ordered[Option[T]] 
//with Numeric[T]
{
  // Proxy.self
  def self: Any = od
  
  // Ordered
  def compare(y: Option[T]): Int = {
    if (od == None) return if (y == None) 0 else 1 
    if (y == None) return -1 
    val Some(odv) = od
    val Some(yv) = y
    return numeric.compare(odv, yv)
  }
  
  def +(o:Option[T]) = if (o==None) o else Some(numeric.plus(od.get,o.get))
  def unary_-() :Option[T] = Some(numeric.negate(od.get)) // Without :Option[T], RichOptionNoneClass unary_-() doesn't compile as it expects to return Some[T]
  def -(o:Option[T]) = if (o==None) o else Some(numeric.minus(od.get,o.get))
  def *(o:Option[T]) = if (o==None) o else Some(numeric.times(od.get,o.get))
  def /(o:Option[T])(implicit numeric: Fractional[T]) = if (o==None) o else Some(numeric.div(od.get,o.get))
  def abs() = if (od==None) od else Some(numeric.abs(od.get))
  def max(o:Option[T]) = if (o==None) o else Some(numeric.max(od.get,o.get))
  def min(o:Option[T]) = if (o==None) o else Some(numeric.min(od.get,o.get))

//  class RichOptionNoneClass extends RichOption[T](None) {
//    override def compare(y: Option[T]): Int = if (y == None) 0 else 1 
//    override def +(o:Option[T]) = None
//    override def unary_-() :Option[T] = None
//    override def -(o:Option[T]) = None
//    override def *(o:Option[T]) = None
//    override def /(o:Option[T])(implicit numeric: Fractional[T]) = None    
//  }
  
  object RichOptionNone extends RichOption[T](None){
    override def compare(y: Option[T]): Int = if (y == None) 0 else 1 
    override def +(o:Option[T]) = None
    override def unary_-() :Option[T] = None
    override def -(o:Option[T]) = None
    override def *(o:Option[T]) = None
    override def /(o:Option[T])(implicit numeric: Fractional[T]) = None      
  }
  
  

  // TODO equals hashcode ? see scala book, but RichDouble does not define equals/hashcode but defines compare
}





