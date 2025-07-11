package com.adnimals.server.model

import io.circe.generic.AutoDerivation
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import org.apache.pekko.http.scaladsl.server.PathMatcher.Lift.MOps.ListMOps

case class Ad(
  id: String,
  title: String,
  content: String,
  slotId: String,
  creativeUrl: Option[String],
  targeting: Map[String, String]
){
  def scoreAgainst(context: Map[String, String]): Int = {
    targeting.count { case (k, v) => context.get(k).contains(v) }
  }
}

object Ad {
  implicit val adEncoder: Encoder[Ad] = deriveEncoder
  implicit val adDecoder: Decoder[Ad] = deriveDecoder
}
