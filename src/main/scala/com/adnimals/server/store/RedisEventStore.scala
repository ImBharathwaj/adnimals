package com.adnimals.server.store

import cats.effect.Sync
import cats.syntax.all._
import dev.profunktor.redis4cats.RedisCommands
import com.adnimals.server.model.AdEvent
import io.circe.generic.auto._       // ✅ Required for automatic Encoder/Decoder
import io.circe.syntax._            // ✅ Required for .asJson
import io.circe.parser.decode

class RedisEventStore[F[_]: Sync](redis: RedisCommands[F, String, String]) {

  private val key = "events"

  def add(event: AdEvent): F[Unit] =
    redis.lPush(key, event.asJson.noSpaces).void

  def list(): F[List[AdEvent]] =
    redis.lRange(key, 0, -1).map(_.flatMap(json => decode[AdEvent](json).toOption))
}
