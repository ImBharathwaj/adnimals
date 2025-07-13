package com.adnimals.server

import cats.effect.{IO, IOApp}
import com.adnimals.server.core.RedisClient
import com.adnimals.server.store.RedisAdStore
import com.adnimals.server.routes.AdRoutes
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import scala.concurrent.ExecutionContextExecutor

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    implicit val system: ActorSystem = ActorSystem("adnimals-system")
    implicit val mat: Materializer = Materializer(system)
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    RedisClient.make[IO].use { redis =>
      val adStore = new RedisAdStore[IO](redis)
      val adRoutes = new AdRoutes(adStore)
      val allRoutes = adRoutes.routes

      for {
        _ <- IO.println("✅ Redis connected.")
        _ <- IO.fromFuture(IO(Http().newServerAt("0.0.0.0", 8080).bind(allRoutes)))
        _ <- IO.never
      } yield ()
    }.handleErrorWith { error =>
      IO.println(s"❌ Redis or server startup failed: ${error.getMessage}")
    }
  }
}
