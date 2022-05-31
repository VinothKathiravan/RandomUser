package com.zoho.randomuser.data

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface RandomUserDao {

    @Query("SELECT * FROM random_user")
    fun getAllUsers(): PagingSource<Int, RandomUser>

    @Query("SELECT * FROM random_user WHERE name LIKE '%' || :query || '%'")
    fun getUserByQuery(query: String): PagingSource<Int, RandomUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsersList(userList: List<RandomUser>)

    @Delete
    fun deleteByQuery(user: RandomUser)

}