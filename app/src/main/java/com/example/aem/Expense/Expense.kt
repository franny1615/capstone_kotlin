package com.example.aem.Expense

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.aem.Transactions.TransactionEntity

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionEntity::class,
        parentColumns = arrayOf("tranId"),
        childColumns = arrayOf("tranId"),
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "expense_table",
    indices = [Index(value = arrayOf("tranId"))]
)
class Expense(var tranId: Long) {
    @PrimaryKey(autoGenerate = true)
    var expenseId = 0
}