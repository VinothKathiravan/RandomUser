package com.zoho.randomuser.data

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface RandomUserDao {

    @Query("SELECT * FROM random_user")
    fun getAllUsers(): PagingSource<Int, RandomUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsersList(userList: List<RandomUser>)

}