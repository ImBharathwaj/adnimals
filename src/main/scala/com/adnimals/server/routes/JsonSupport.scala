package com.adnimals.server.routes

import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.{Decoder, Encoder}

trait JsonSupport extends FailFastCirceSupport {
  implicit def encoder[A: Encoder]: Encoder[A] = implicitly
  implicit def decoder[A: Decoder]: Decoder[A] = implicitly
}
