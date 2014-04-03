package etf.analyzer

import org.scala_tools.option.math.Imports._  


case class Summary  (
  ticker : String, name : String, assetClass : String,
  assetSubClass : String, weekly : Option[Double], 
  fourWeeks : Option[Double], threeMonths : Option[Double], 
  sixMonths : Option[Double], oneYear : Option[Double],
  stdDev : Double, price : Double, mav200 : Double 
) {

  // Abracadabra ...
  val LRS = (fourWeeks + threeMonths + sixMonths + oneYear) / 4 
  
  def banner() = {
    printf("%-6s", "Ticker")
    printf("%-50s", "Name")
    printf("%-12s", "Asset Class")
    //printf("%-30s\t", "Asset SubClass";
    printf("%4s", "RS")
    printf("%4s", "1Wk")
    printf("%4s", "4Wk")
    printf("%4s", "3Ms")
    printf("%4s", "6Ms")
    printf("%4s", "1Yr")
    printf("%6s", "Vol")
    printf("%2s\n", "Mv")
    //printf("%6s", "Pr")
    //printfln("%6s", "M200")
  }
  
  def print() = {
  
    printf("%-6s", ticker)
    printf("%-50s", new String(name.toArray.take(48)))
    printf("%-12s", new String(assetClass.toArray.take(10)))
    //printf("%-30}\t", new String(AssetSubClass.Take(28).ToArray()));
    printf("%4.0f", LRS * 100 getOrElse null)
    printf("%4.0f", weekly * 100 getOrElse null)
    printf("%4.0f", fourWeeks * 100 getOrElse null)
    printf("%4.0f", threeMonths * 100 getOrElse null)
    printf("%4.0f", sixMonths * 100 getOrElse null)
    printf("%4.0f", oneYear * 100 getOrElse null)
    printf("%6.0f", stdDev * 100)
    if (price <= mav200) {
      printf("%2s\n", "X");
    } else {
      println();
    }
    //printf("%6f}", Price);
    //printfln("%6f}", Mav200);
  }
  
}
