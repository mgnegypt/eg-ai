package com.mgn.ai.data.favorite

import com.mgn.ai.data.db.entity.FavoriteEntity
import com.mgn.ai.data.model.FavoriteType

interface FavoriteAdapter<T> {
    val type: FavoriteType

    fun buildRefKey(target: T): String

    fun buildFavoriteEntity(
        target: T,
        existing: FavoriteEntity? = null,
        now: Long = System.currentTimeMillis()
    ): FavoriteEntity
}
