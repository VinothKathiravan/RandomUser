package com.zoho.randomuser.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zoho.randomuser.api.RandomUserService

private const val STARTING_PAGE_INDEX = 1

class RandomUserPagingSource(
    private val service: RandomUserService,
    private val query: Int
) : PagingSource<Int, RandomUser>() {

    override fun getRefreshKey(state: PagingState<Int, RandomUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RandomUser> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.fetchUsers(query)
            Log.i("RESPONSE", response.toString())
            val users = response.results
            LoadResult.Page(
                data = users,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}