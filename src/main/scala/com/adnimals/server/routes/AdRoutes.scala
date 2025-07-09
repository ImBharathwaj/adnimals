package com.adnimals.server.routes

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Route
import com.adnimals.server.model.Ad
import io.circe.syntax._
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport._

class AdRoutes {
  private val ads: List[Ad] = List(
    Ad("1", "https://cdn.example.com/ad1.jpg", "https://click.example.com/1", "home_top", Map("geo" -> "IN")),
    Ad("2", "https://cdn.example.com/ad2.jpg", "https://click.example.com/2", "home_top", Map("geo" -> "US")),
    Ad("3", "https://cdn.example.com/ad3.jpg", "https://click.example.com/3", "sidebar", Map("geo" -> "IN"))
  )

  val routes: Route =
    pathPrefix("ad") {
      get {
        parameter("slot_id", "geo".?) { (slotId, geoOpt) =>
          val filtered = ads.filter(ad =>
            ad.slotId == slotId && geoOpt.forall(geo => ad.targeting.get("geo").contains(geo))
          )
          filtered.headOption match {
            case Some(ad) => complete(ad)
            case None     => complete(StatusCodes.NoContent)
          }
        }
      }
    } ~
      path("health") {
        get {
          complete("OK")
        }
      }
}
