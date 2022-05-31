package com.zoho.randomuser.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bumptech.glide.load.HttpException
import com.zoho.randomuser.api.RandomUserService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RandomUserMediator(
    private val service: RandomUserService,
    private val userDao: RandomUserDao,
    private val query: Int
) : RemoteMediator<Int, RandomUser>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RandomUser>
    ): MediatorResult {
        return try {

            when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    lastItem.userId
                }
            }

            val response = service.fetchUsers(query)
            userDao.insertAllUsersList(response.results)
            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}