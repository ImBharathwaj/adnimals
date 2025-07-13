package com.adnimals.server.service

import cats.effect.Sync
import cats.syntax.all._
import dev.profunktor.redis4cats.RedisCommands
import scala.concurrent.duration.DurationInt

class FrequencyCapService[F[_]: Sync](redis: RedisCommands[F, String, String]) {

  // TTL for cap, e.g., 1 hour
  private val ttlSeconds = 3600

  def isCapped(userId: String, adId: String): F[Boolean] = {
    val key = s"fcap:$userId:$adId"
    redis.exists(key)
  }

  def markServed(userId: String, adId: String): F[Unit] = {
    val key = s"fcap:$userId:$adId"
    redis.setEx(key, "1", ttlSeconds.seconds).void
  }
}
