package com.github.deviants.wisewallet.data.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
//transaction model
@Entity(tableName = "transaction" )
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name = "transactionType")
    val transactionType: String = EXPENSE,
    @ColumnInfo(name = "category")
    val transactionCategory: String,
    @ColumnInfo(name = "price")
    val transactionPrice: Double,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "compulsory")
    val isCompulsory: Boolean = true,
    @ColumnInfo(name = "description")
    val transactionDescription: String = transactionCategory,
    var isFirstInDay: Boolean = false

) {
    companion object {
        const val EXPENSE = "expense"
        const val INCOME = "income"
        const val ALL_TRANSACTIONS = "all"
    }
}



fun Transaction.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(transactionPrice)