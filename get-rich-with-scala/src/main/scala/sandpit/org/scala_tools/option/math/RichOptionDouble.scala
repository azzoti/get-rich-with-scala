//// Modelled on Martin Odersky pimp my library technique 
//// http://www.artima.com/weblogs/viewpost.jsp?thread=179766
//
//class RichOptionDouble(od:Option[Double])(implicit numeric: Numeric[Double]) extends Proxy with Ordered[Option[Double]] {
//  
//  // Proxy.self
//  def self: Any = od
//  
//  def compare(y: Option[Double]): Int = {
//    if (od == None) return if (y == None) 0 else 1 
//    if (y == None) return -1 
//    val Some(odv) = od
//    val Some(yv) = y
//    return numeric.compare(odv, yv)
//  }
//
//  def +(o:Option[Double]) = if (o==None) o else Some(numeric.plus(od.get,o.get))
//  def unary_-():Option[Double] = Some(-od.get) // Without :Option[Double], RichNoneDouble unary_-() doesn't compile as it expects to return Some[Double]
//  def -(o:Option[Double]) = if (o==None) o else Some(od.get-o.get)
//  def *(o:Option[Double]) = if (o==None) o else Some(od.get*o.get)
//  def /(o:Option[Double]) = if (o==None) o else Some(od.get/o.get)
//}
//
//object RichOptionDoubleNone extends RichOptionDouble(None) { 
//  override def +(o:Option[Double]) = None
//  override def unary_-() = None
//  override def -(o:Option[Double]) = None
//  override def *(o:Option[Double]) = None
//  override def /(o:Option[Double]) = None
//}