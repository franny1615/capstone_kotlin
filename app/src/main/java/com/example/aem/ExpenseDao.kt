package com.example.aem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    fun insertExpense(v : Expense) : Long

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses() : List<Expense>
}