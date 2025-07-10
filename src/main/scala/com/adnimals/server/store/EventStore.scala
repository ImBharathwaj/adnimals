package com.adnimals.server.store

import com.adnimals.server.model.AdEvent
import scala.collection.mutable.ListBuffer

object EventStore {
  private val events = ListBuffer.empty[AdEvent]

  def log(event: AdEvent): Unit = synchronized {
    events += event
  }

  def list(): List[AdEvent] = events.toList

  def countByAd(): Map[String, Int] =
    events.groupBy(_.adId).view.mapValues(_.size).toMap

  def countClicksByAd(): Map[String, Int] =
    events.filter(_.eventType == "click").groupBy(_.adId).view.mapValues(_.size).toMap

  def countImpressionsByAd(): Map[String, Int] =
    events.filter(_.eventType == "impression").groupBy(_.adId).view.mapValues(_.size).toMap
}