package com.zoho.randomuser.data

import androidx.paging.*
import com.zoho.randomuser.api.RandomUserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RandomUserRepository @Inject constructor(
    private val randomUserDao: RandomUserDao,
    private val service : RandomUserService
    ) {

    fun getSearchResultStream(): Flow<PagingData<RandomUser>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { RandomUserPagingSource(service, NETWORK_PAGE_SIZE) }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getRandomUserResultStream(): Pager<Int, RandomUser> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = RandomUserMediator(service, randomUserDao, NETWORK_PAGE_SIZE)
        ) {
            randomUserDao.getAllUsers()
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }
}