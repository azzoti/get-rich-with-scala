package etf.analyzer

import scala.io.Source
import org.scala_tools.time.Imports._
import org.scala_tools.using.Using 
import org.scala_tools.web.WebClient
import org.scala_tools.web.WebClientConnections
import org.scala_tools.web.DownloadStringCompletedEventArgs 
import java.io.File
import java.util.concurrent.CountDownLatch 

case class Event (date : DateTime, price : Double) {}

object Program extends Using 
{

  def createUrl(ticker: String, start: DateTime, end: DateTime) : String = {
    return """http://ichart.finance.yahoo.com/table.csv?s=""" + ticker +
      "&a="+(start.month.get-1)+ "&b=" + start.day.get + "&c=" + start.year.get +
      "&d="+(end.month.get  -1)+ "&e=" + end.day.get + "&f=" + end.year.get +
      "&g=d&ignore=.csv"
  }	
  
  def main(args : Array[String]) : Unit = {
    
    val tickers = 
    	Source.fromFile(new File("ETFs.csv")).getLines()
    	.drop(1)
    	.map(l => l.trim.split(','))
    	.filter(v => v(2) != "Leveraged")
    	.map(values => (values(0),values(1),values(2),if (values.length==4) values(3) else ""))
    	.toSeq.toArray

    tickers foreach println
    
    val len = tickers.length;

    val start = DateTime.now - 2.years
    val end = DateTime.now
    val cevent = new CountDownLatch(len)
    val summaries = new Array[Summary](len)
    
    using(new WebClientConnections(connectionsPerAddress = 10, threadPool=10)) { // default timeout
      
      webClientConnections =>

      for (i <- 0 until len) {   
  
      	val t = tickers(i)
      	// println(t)
        val url = createUrl(t._1, start, end);
      	val webClient = webClientConnections.getWebClient
        webClient.DownloadStringCompleted += downloadStringCompleted;
        webClient.DownloadStringAsync(url, (t, cevent, summaries, i));
  		  
      }
      
      cevent.await()
      
      // The using block will shut down the WebClientConnections thread pool 
    }
    
    println
    
    val top15perc =
      summaries
      .filter(s => s.LRS.isDefined)
      .sortBy(elem1 => elem1.LRS.get).reverse
      .take((len * 0.15).toInt)

    val bottom15perc =
      summaries
      .filter(s => s.LRS.isDefined)
      .sortBy(elem1 => elem1.LRS.get)
      //.sortWith((elem1, elem2) => elem1.LRS.get <= elem2.LRS.get)
      .take((len * 0.15).toInt)

    println
    summaries(0).banner()
    println("TOP 15%")
    for (s <- top15perc) 
      s.print() 

    println
    println("Bottom 15%")
    for (s <- bottom15perc) 
      s.print()
	
  }

  def downloadStringCompleted(e: DownloadStringCompletedEventArgs) {
    val bigTuple = e.userState.asInstanceOf[Tuple4[Tuple4[String, String, String, String], CountDownLatch, Array[Summary], Int]]

    // assignment using pattern matching
    val ((ticker, name, asset, subAsset), cevent, summaries, i) = bigTuple

    def parse(s: String) = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(s);

    if (e.error == null) {
      val adjustedPrices =
        e.result
          .split('\n')
          .drop(1)
          .map(l => l.split(','))
          .filter(l => l.length == 7)
          .map(v => Event(parse(v(0)), v(6).toDouble))

      val timeSeries = new TimeSeries(ticker, name, asset, subAsset, adjustedPrices);
      summaries(i) = timeSeries.getSummary();
      cevent.countDown()
      printf("%s ", ticker)
    } else {
      printf("[%s ERROR] \n", ticker)
      //Console.WriteLine(e.Error);
      summaries(i) = Summary(ticker, name, "ERROR", "ERROR", Some(0), Some(0), Some(0), Some(0), Some(0), 0, 0, 0)
      cevent.countDown()
    }
  }
  
}
