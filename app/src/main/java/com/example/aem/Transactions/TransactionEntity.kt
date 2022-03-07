package com.example.aem.Transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDate

@Entity(tableName = "transactions_table_aem")
class TransactionEntity(@ColumnInfo(name = "item_id") var itemId: String,
                        var date: String,
                        var category: String,
                        var merchant: String,
                        var amount: Double
                        ) {
    @PrimaryKey(autoGenerate = true)
    var tranId: Long = 0
}