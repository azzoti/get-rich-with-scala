package org.scala_tools.web

import org.scala_tools.event.EventHandler
import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.ContentExchange
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.client.HttpExchange
import org.eclipse.jetty.io.Buffer

case class DownloadStringCompletedEventArgs(userState: AnyRef, result: String, error: String) {}

case class WebClientConnections(connectionsPerAddress: Int, threadPool : Int, timeout: Int = 30000) extends HttpClient{

  setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL)
  setMaxConnectionsPerAddress(connectionsPerAddress) // max concurrent connections to every address
  setThreadPool(new QueuedThreadPool(threadPool)) // max threads
  setTimeout(timeout) // 30 seconds timeout; if no server reply, the request expires
  start();

  def getWebClient() = new WebClient(this)

  def close() = stop() // could check active web clients first?

}

class WebClient(connections: WebClientConnections) {

  // Event handler: a bit like a C# event
  val DownloadStringCompleted = new EventHandler[DownloadStringCompletedEventArgs]()

  def DownloadStringAsync(url : String, userToker : AnyRef) = {
    val exchange = new ContentExchange(/* keep headers */false)
    {
      override def onResponseComplete() = {
        if (getResponseStatus == 200) {
          DownloadStringCompleted(new DownloadStringCompletedEventArgs(userToker,getResponseContent,null))
        } else {
          DownloadStringCompleted(new DownloadStringCompletedEventArgs(userToker,null,"ERROR"))
        }
      }
    };  
    exchange.setURL(url);
    connections.send(exchange);    
  }
  
}
