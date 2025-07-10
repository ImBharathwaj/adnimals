package com.adnimals.server.model

case class AdEvent(
  adId: String,
  eventType: String,
  timestamp: Long,
  meta: Map[String, String]
)