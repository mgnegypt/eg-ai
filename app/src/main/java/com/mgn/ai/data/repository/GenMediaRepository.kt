package com.mgn.ai.data.repository

import androidx.paging.PagingSource
import com.mgn.ai.data.db.dao.GenMediaDAO
import com.mgn.ai.data.db.entity.GenMediaEntity

class GenMediaRepository(private val dao: GenMediaDAO) {
    fun getAllMedia(): PagingSource<Int, GenMediaEntity> = dao.getAll()

    suspend fun insertMedia(media: GenMediaEntity) = dao.insert(media)

    suspend fun deleteMedia(id: Int) = dao.delete(id)
}
