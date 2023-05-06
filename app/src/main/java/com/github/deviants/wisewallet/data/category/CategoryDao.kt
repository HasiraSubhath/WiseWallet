package com.github.deviants.wisewallet.data.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE FROM `categories`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `categories` WHERE transactionType = :type")
    fun getCategoriesByTransactionType(type: String): Flow<List<Category>>
}