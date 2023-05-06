package com.github.deviants.wisewallet.data.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories" )
data class Category (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name = "transactionType")
    val transactionType: String,
    @ColumnInfo(name = "category")
    val categoryName: String,
    @ColumnInfo(name = "isActive")
    var isActive: Boolean = false
)