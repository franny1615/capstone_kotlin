package com.example.aem.Expense

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aem.Transactions.TransactionEntity

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertExpense(v: Expense): Long

    @Query("DELETE FROM expense_table WHERE tranId = :id")
    fun deleteExpenseByTranId(id: String): Int

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): List<Expense>

    @Query("SELECT * FROM expense_table")
    fun getAllExpensesLiveData(): LiveData<List<Expense>>

    @Query("SELECT transactions_table_aem.tranId,item_id,date,category,merchant,amount FROM transactions_table_aem INNER JOIN expense_table ON transactions_table_aem.tranId = expense_table.tranId ORDER BY transactions_table_aem.date DESC")
    fun getExpensesAsTransactionList(): List<TransactionEntity>
}