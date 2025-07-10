package com.adnimals.server.model

case class AdStats(
                    adId: String,
                    impressions: Int,
                    clicks: Int
                  )

case class StatsResponse(
                          stats: List[AdStats]
                        )
