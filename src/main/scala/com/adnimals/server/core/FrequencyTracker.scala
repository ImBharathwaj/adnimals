package com.adnimals.server.core

import java.time.Instant
import scala.collection.mutable

object FrequencyTracker {
  // Map: (userId, adId) → lastServedTimestamp
  private val seen = mutable.Map[(String, String), Long]()
  private val timeWindowSeconds = 3600 // 1 hour

  def canServe(userId: String, adId: String): Boolean = {
    val now = Instant.now().getEpochSecond
    seen.get((userId, adId)) match {
      case Some(lastSeen) if now - lastSeen < timeWindowSeconds => false
      case _ =>
        seen.update((userId, adId), now)
        true
    }
  }
}
