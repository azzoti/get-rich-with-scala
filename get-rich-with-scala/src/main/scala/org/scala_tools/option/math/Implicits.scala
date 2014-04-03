package org.scala_tools.option.math

// Modeled on technique in Scala-Time

object Implicits extends Implicits

trait Implicits {
  implicit def optiondouble2richoptiondouble(od: Option[Double]) = {
    if (od == None) RichNoneDouble else new RichOptionDouble(od)
  }
  implicit def double2optiondouble(d: Double) = Some(d)
  implicit def int2optiondouble(i: Int) = Some(i.toDouble)
}