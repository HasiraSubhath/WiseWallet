package com.github.deviants.wisewallet.data.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
//dao for transactions
@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getItem(id: Int): Flow<Transaction>

    @Query("SELECT * FROM `transaction` WHERE transactionType = :type ORDER BY date DESC")
    fun getItemsByType(type: String): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getItems(): Flow<List<Transaction>>
}