package com.adnimals.server.store

import com.adnimals.server.model.Ad
import com.adnimals.server.core.FrequencyTracker

object AdStore {

  // In-memory ad list
  private var ads: List[Ad] = List()

  // Add or update an ad
  def create(ad: Ad): Unit = {
    ads = ads.filterNot(_.id == ad.id) :+ ad
  }

  // Get all ads
  def list(): List[Ad] = ads

  // Get ad by ID
  def getById(adId: String): Option[Ad] = {
    ads.find(_.id == adId)
  }

  // Delete ad by ID
  def delete(adId: String): Boolean = {
    val original = ads
    ads = ads.filterNot(ad => ad.id == adId)
    original.length != ads.length
  }

//  def getBestMatch(slotId: String, context: Map[String, String]): Option[Ad] = {
//    val candidates = ads.filter(_.slotId == slotId)
//    candidates
//      .filter(ad => ad.targeting.forall { case (k, v) => context.get(k).contains(v) })
//      .sortBy(ad => -ad.scoreAgainst(context))
//      .headOption
//  }

  def getBestMatch(slotId: String, context: Map[String, String]): Option[Ad] = {
    val userIdOpt = context.get("userId")
    val candidates = ads.filter(_.slotId == slotId)

    val eligible = candidates
      .filter(ad => ad.targeting.forall { case (k, v) => context.get(k).contains(v) })
      .filter(ad => userIdOpt.forall(userId => FrequencyTracker.canServe(userId, ad.id)))

    eligible
      .sortBy(ad => -(ad.scoreAgainst(context)))
      .headOption
  }

}