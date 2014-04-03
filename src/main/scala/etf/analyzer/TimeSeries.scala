package etf.analyzer

import org.scala_tools.time.Imports._
//import org.scala_tools.option.math.Imports._  
import org.joda.time.Days


// Assumption in C# code that the events are in date order with the oldest date last
case class TimeSeries (
    ticker : String, name : String, assetClass : String, 
    assetSubClass : String, events : Iterable[Event]
) {

  // c# has _adjDictionary = events.ToDictionary(e => e.Date, e => e.Price)
  // which is slightly more succinct
  private val _adjDictionary : Map[DateTime, Double] 
              = Map() ++ events.map(e => (e.date -> e.price))
  private val _start = events.last.date
  
  // Returns None if no price found
  // or Some((price:Double,daysBefore:Int)) tuple of price and days before requested date that a price was found
  def getPrice2(when : DateTime) : Option[(Double,Int)] =  {
    // Find the most recent day with a price starting from when, but don't go back past _start 
    val latestDayWithPrice 
        = Iterator.iterate(when)(_ - 1.days)
          .dropWhile (d=> !_adjDictionary.contains(d) && d >= _start )
          .next
    if (_adjDictionary.contains(latestDayWithPrice)) {
      val shift = Days.daysBetween(when,latestDayWithPrice).getDays()
      val aPrice = _adjDictionary(latestDayWithPrice) 
      Some((aPrice,shift))
    } else {
      None
    }
  }  

  // Returns 
  //    None if no price found
  // or 
  //    Some((price:Double,shift:Int)) 
  //    where price is the most recent price found 
  //    and shift is the number of days before the requested date that a price was found
  //    -1 means one day before
  def getPrice(whenp : DateTime) : Option[(Double,Int)] =  {
    var when = new DateTime(whenp.year.get,whenp.month.get,whenp.day.get,0,0,0,0)
    var found = false
    var shift = 1
    var aPrice = 0.0
    while (when >= _start && !found) {
        if (_adjDictionary.contains(when)) {
            aPrice = _adjDictionary(when)
            found = true
        }
        when = when - 1.days
        shift -= 1
    }
    // Either return the price and the shift or None if no price was found
    if (found) Some(aPrice,shift) else return None
  }  
  
  def getReturn(start: DateTime, end: DateTime) : Option[Double] = {
    var endPriceDetails = getPrice(end)
    if (endPriceDetails == None) return None
    val (endPrice,daysBefore) = endPriceDetails.getOrElse(null)
    val startPriceDetails = getPrice(start + daysBefore.days)
    if (startPriceDetails == None) return None
    val (startPrice,_) = startPriceDetails.getOrElse(null)
    Some(endPrice / startPrice - 1.0)      
  }
  
  def getReturn3(start: DateTime, end: DateTime) : Option[Double] = {
    // Below, the 2 calls to getPrice() return an Option[a price, days offset]
    // Dealing with Option[...] in a for expression is an easy way of dealing with the 
    // possibilty of either Option[...] being None 
    // If either getPrice() call returns None, then the yield will return a None as well.

    for {
      (endPrice,daysBefore) <- getPrice(end) 
      (startPrice,_) <- getPrice(start + daysBefore.days)
    } yield 
      (endPrice / startPrice - 1.0) // yield the average price increase over the period      
      
  }
  
  def getReturn2(start: DateTime, end: DateTime) : Option[Double] = {
    getPrice(end) match {
      case None => 
        return None
      case Some((endPrice,daysBefore)) => 
        getPrice(start + daysBefore.days) match {
          case None =>
            return None
          case Some((startPrice,_)) => 
            Some(endPrice / startPrice - 1.0)
      }
    }      
  }
  
  private def lastWeekReturn   
    = getReturn(DateTime.now - 7.days, DateTime.now)

  private def last4WeeksReturn 
    = getReturn(DateTime.now - 28.days, DateTime.now)
  
  private def last3MonthsReturn
    = getReturn(DateTime.now - 3.months, DateTime.now)
  
  private def last6MonthsReturn
    = getReturn(DateTime.now - 6.months, DateTime.now)
  
  private def lastYearReturn   
    = getReturn(DateTime.now - 1.years, DateTime.now) 
  
  // Add the sum and average function to all Iterables[Double] used locally
  private implicit def iterableWithSumAndAverage(c: Iterable[Double]) = new { 
    def sum = c.foldLeft(0.0)(_ + _) 
    def average = sum / c.size
  }  
  def stdDev(): Double = {
    val today = DateTime.now
    val limit = today - 3.years
    val dates = Iterator.iterate(today)(_ - 7.days)
                .takeWhile (d => d >= (_start + 12.days) && d >= limit)
                .toList
    val rets = dates.map(d => getReturn(d - 7.days, d).get)
    val mean = rets.average
    val variance = rets.map(r => Math.pow(r - mean, 2)).average
    val weeklyStdDev = Math.sqrt(variance);
    return weeklyStdDev * Math.sqrt(40);
  }
  def mav200(): Double = {
    return _adjDictionary.toList
          .sortWith((elem1, elem2) => elem1._1 >= elem2._1)
          //.sortBy(elem1 => elem1._1)
          .take(200).map(keyValue => keyValue._2).average
  }
  def todayPrice() : Double = {
    // Match the rather cavalier c# algorithm
    getPrice(DateTime.now) match {
      case None => 0.0
      case Some((price,_)) => price 
    }
  }
  def getSummary() = {
    //println(Date.today().toString("dd/MM/yyyy"))
    //todayPrice()
    //    println("r1:"+lastWeekReturn())
    //    println("r2:"+last4WeeksReturn())
    //    println("r3:"+last3MonthsReturn())
    //    println("r4:"+last6MonthsReturn())
    //    println("r5:"+lastYearReturn())
    // println("stddev:" + stdDev());
    // println("mav200:" + mav200());
    
    Summary(
      ticker, name, assetClass, assetSubClass, 
      lastWeekReturn, last4WeeksReturn, last3MonthsReturn, 
      last6MonthsReturn, lastYearReturn, stdDev, todayPrice, 
      mav200)
  }
}

