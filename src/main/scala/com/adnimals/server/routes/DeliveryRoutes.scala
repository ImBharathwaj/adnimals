package com.adnimals.server.routes

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.model.StatusCodes

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.concurrent.ExecutionContext
import cats.syntax.all._

import com.adnimals.server.store.RedisAdStore
import com.adnimals.server.service.FrequencyCapService
import com.adnimals.server.model.Ad
import io.circe.syntax._

class DeliveryRoutes(
                      adStore: RedisAdStore[IO],
                      freqCap: FrequencyCapService[IO]
                    )(implicit ec: ExecutionContext) extends JsonSupport {

  val routes: Route = path("serve") {
    parameters("slotId", "geo".?, "device".?) { (slotId, geo, device) =>
      val context = Map("geo" -> geo, "device" -> device).collect {
        case (k, Some(v)) => k -> v
      }

      val userId = context.getOrElse("device", "unknown") // fallback ID

      val resultF: IO[Option[Ad]] = adStore.list().flatMap { ads =>
        val filtered = ads.filter(_.slotId == slotId)
        filtered.findF { ad =>
          freqCap.isCapped(userId, ad.id).map(!_).widen
        }.flatTap {
          case Some(ad) => freqCap.markServed(userId, ad.id)
          case None     => IO.unit
        }
      }

      onSuccess(resultF.unsafeToFuture()) {
        case Some(ad) => complete(ad.asJson)
        case None     => complete(StatusCodes.NotFound -> "No eligible ad found")
      }
    }
  }
}