package com.adnimals.server

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.stream.Materializer
import com.adnimals.server.routes.AdRoutes

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("AdnimalSystem")
  implicit val materializer: Materializer = Materializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val routes = new AdRoutes().routes

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(routes)

  bindingFuture.onComplete {
    case Success(_) =>
      val ip = java.net.InetAddress.getLocalHost.getHostAddress
      println(s"Server online at http://$ip:8080")
    case Failure(ex) =>
      println(s"Failed to bind HTTP endpoint, terminating system: $ex")
      system.terminate()
  }
}

//object Main extends App{
//  println("Working!")
//}