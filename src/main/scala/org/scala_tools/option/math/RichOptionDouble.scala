//package org.scala_tools.option.math
//
//// Modelled on Martin Odersky pimp my library technique
//// http://www.artima.com/weblogs/viewpost.jsp?thread=179766
//
//class RichOptionDouble(od: Option[Double]) {
//  def +(o: Option[Double]) = if (o == None) o else Some(od.get + o.get)
//  // Without the :Option[Double] in the definition of unary_-(),   RichNoneDouble unary_- () 
//  // doesn't compile as it expects to return Some[Double]
//  def unary_-(): Option[Double] = Some(-od.get)
//  def -(o: Option[Double]) = if (o == None) o else Some(od.get - o.get)
//  def *(o: Option[Double]) = if (o == None) o else Some(od.get * o.get)
//  def /(o: Option[Double]) = if (o == None) o else Some(od.get / o.get)
//}
//
//object RichNoneDouble extends RichOptionDouble(None) {
//  override def +(o: Option[Double]) = None
//  override def unary_-() = None
//  override def -(o: Option[Double]) = None
//  override def *(o: Option[Double]) = None
//  override def /(o: Option[Double]) = None
//}