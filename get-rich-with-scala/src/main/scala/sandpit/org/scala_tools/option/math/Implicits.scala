//package sandpit.org.scala_tools.option.math
//
//
//trait Implicits {
//  
//  class RichOptionDouble(o:Option[Double]) extends RichOption(o) {}
//  class RichOptionInt(o:Option[Int]) extends RichOption(o) {
//    def toOptionDouble():Option[Double] = o match { case None => None:Option[Double] case Some(v) => Some(v.toDouble):Option[Double] }
//  }
//  
//  private object RichOptionDoubleNoneOuter extends RichOptionDouble(None)  // annoying extra object required!
//  private object RichOptionIntNoneOuter extends RichOptionInt(None)  // annoying extra object required!
//  
//  implicit def optiondouble2richoptiondouble(o:Option[Double]) =  
//    o match { case None => RichOptionDoubleNoneOuter.RichOptionNone case _ => new RichOptionDouble(o) }
//  implicit def optionfloat2richoptiondouble(o:Option[Float]) = 
//    o match { case None => RichOptionDoubleNoneOuter.RichOptionNone case Some(v) => new RichOptionDouble(Some(v.toDouble)) }
//  implicit def optionlong2richoptiondouble(o:Option[Long]) = 
//    o match { case None => RichOptionDoubleNoneOuter.RichOptionNone case Some(v) => new RichOptionDouble(Some(v.toDouble)) }
//  implicit def optionint2richoptiondouble(o:Option[Int]) = 
//    o match { case None => RichOptionDoubleNoneOuter.RichOptionNone case Some(v) => new RichOptionDouble(Some(v.toDouble)) }
//  
//  implicit def optionint2richoptionint(o:Option[Int]):RichOptionInt = new RichOptionInt(o)
//  
//  implicit def double2optiondouble(d:Double) = Some(d) 
//  implicit def float2optiondouble(f:Float) = Some(f.toDouble)
//  implicit def long2optiondouble(l:Long) = Some(l.toDouble)
//  implicit def int2optiondouble(i:Int) = Some(i.toDouble)
//
//  implicit def int2optionint(i:Int) = Some(i)
//
//  
//  // These work for a literal at the *beginning* of an expression involving Option[T]
//  implicit def double2richoptiondouble(v:Double) = new RichOptionDouble(Some(v)) 
//  implicit def float2richoptiondouble(v:Float) = new RichOptionDouble(Some(v.toDouble)) 
//  implicit def long2richoptiondouble(v:Long) = new RichOptionDouble(Some(v.toDouble)) 
//  implicit def int2richoptiondouble(v:Int) = new RichOptionDouble(Some(v.toDouble)) 
//
//  implicit def int2richoptionint(v:Int) = new RichOptionInt(Some(v))
//  
//  // int to double upgrades
//  implicit def optionint2optiondouble(v:Option[Int]) = v match { case None => None:Option[Double] case Some(v) => Some(v):Option[Double] }
//  
//
//  
//}