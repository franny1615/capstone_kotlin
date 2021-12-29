package com.example.aem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions_table_aem")
class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    var tranId: Long = 0
    @ColumnInfo(name = "item_id")
    var itemId: String? = null
    var date: String? = null
    var category: String? = null
    var merchant: String? = null
    var amount = 0.0
}