package com.adnimals.server.store

import com.adnimals.server.model.Ad
import java.util.UUID
import scala.collection.concurrent.TrieMap

object AdStore {
  // In-memory concurrent map: adId -> Ad
  private val ads = TrieMap.empty[String, Ad]

  // Create ad with generated UUID
  def create(ad: Ad): Ad = {
    val idWithUUID = ad.copy(id = UUID.randomUUID().toString)
    ads.put(idWithUUID.id, idWithUUID)
    idWithUUID
  }

  // List all ads
  def list(): List[Ad] = ads.values.toList

  // Delete by ID
  def delete(id: String): Boolean = ads.remove(id).isDefined

  // Optional: get ad by ID
  def get(id: String): Option[Ad] = ads.get(id)
}
