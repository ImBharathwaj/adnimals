package com.adnimals.server.core

import cats.effect._
import dev.profunktor.redis4cats._
import dev.profunktor.redis4cats.effect.Log.Stdout.instance
import dev.profunktor.redis4cats.connection._

object RedisClient {
  def make[F[_]: Async]: Resource[F, RedisCommands[F, String, String]] =
    Redis[F].utf8("redis://localhost")
}
