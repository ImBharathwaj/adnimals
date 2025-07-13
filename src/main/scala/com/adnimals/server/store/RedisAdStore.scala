package com.adnimals.server.store

import cats.effect._
import cats.implicits._
import cats.effect.Sync
import io.circe.syntax._
import io.circe.parser.decode
import com.adnimals.server.model.Ad
import dev.profunktor.redis4cats.RedisCommands

class RedisAdStore[F[_]: Sync](redis: RedisCommands[F, String, String]){

  private val adKeyPrefix = "ad:"
  private val adIndexKey = "ads:index"

  def add(ad: Ad): F[Unit] =
    for {
      _ <- redis.set(adKeyPrefix + ad.id, ad.asJson.noSpaces)
      _ <- redis.sAdd(adIndexKey, ad.id)
    } yield ()

  def getAll: F[List[Ad]] =
    for {
      ids <- redis.sMembers(adIndexKey)
      ads <- ids.toList.traverse(id =>
        redis.get(adKeyPrefix + id).map(_.flatMap(json => decode[Ad](json).toOption))
      )
    } yield ads.flatten

  def delete(adId: String): F[Boolean] =
    for {
      removed <- redis.del(adKeyPrefix + adId)
      _       <- redis.sRem(adIndexKey, adId)
    } yield removed > 0
}
