package com.github.deviants.wisewallet.data.transaction

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//room db for transaction
@Database(entities = [Transaction::class], version = 2, exportSchema = false)
abstract class TransactionRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): TransactionDao

    companion object {
        private var INSTANCE: TransactionRoomDatabase? = null
        fun getDatabase(context: Context): TransactionRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionRoomDatabase::class.java,
                    "transaction_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}