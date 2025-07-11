package com.adnimals.server.model

import java.time.Instant
case class AdEvent(
  adId: String,
  eventType: String,
  timestamp: Long = Instant.now().getEpochSecond,
  meta: Option[Map[String, String]]
)