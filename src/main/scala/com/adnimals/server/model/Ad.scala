package com.adnimals.server.model

import io.circe.generic.AutoDerivation
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class Ad(
               id: String,
               creativeUrl: String,
               clickUrl: String,
               slotId: String,
               targeting: Map[String, String]
             )

object Ad {
  implicit val adEncoder: Encoder[Ad] = deriveEncoder
  implicit val adDecoder: Decoder[Ad] = deriveDecoder
}
