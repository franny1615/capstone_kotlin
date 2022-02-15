package com.example.aem.Expense

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertExpense(v : Expense) : Long

    @Query("DELETE FROM expense_table WHERE tranId = :id")
    fun deleteExpenseByTranId(id : String) : Int

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses() : List<Expense>

    @Query("SELECT * FROM expense_table")
    fun getAllExpensesLiveData() : LiveData<List<Expense>>
}