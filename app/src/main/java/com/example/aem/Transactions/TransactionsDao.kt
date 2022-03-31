package com.example.aem.Transactions

import androidx.room.*

@Dao
interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(t: TransactionEntity): Long

    @Query("DELETE FROM transactions_table_aem WHERE item_id = :item_id")
    fun deleteTransactionsByItemId(item_id: String): Int

    @Query("SELECT * FROM transactions_table_aem WHERE item_id = :item_id ORDER BY date DESC")
    fun loadAllTransactionsByItemId(item_id : String) : List<TransactionEntity>

    @Query("SELECT * FROM transactions_table_aem WHERE tranId = :trans_id")
    fun getbyTransactionId(trans_id : String) : TransactionEntity

    @Query("SELECT category,SUM(amount) as 'amount' FROM transactions_table_aem WHERE item_id = :item_id GROUP BY category")
    fun getCategoryTotals(item_id: String) : List<CategoryTotal>

    @Query("SELECT category,SUM(amount) as 'amount' FROM transactions_table_aem INNER JOIN expense_table ON transactions_table_aem.tranId = expense_table.tranId GROUP BY category")
    fun getEntireCategoryTotals() : List<CategoryTotal>

    @Query("SELECT * FROM transactions_table_aem WHERE (item_id = :item_id) AND (category = :category)")
    fun getTransactionsBasedOnCategoryAndItemId(item_id: String, category: String) : List<TransactionEntity>

    @Query("SELECT  category, SUM(amount) as 'amount' FROM transactions_table_aem INNER JOIN expense_table ON transactions_table_aem.tranId = expense_table.tranId WHERE (date <= :end) AND (date >= :start) GROUP BY category")
    fun getCategoryTotalsInATimeFrame(start : String, end : String) : List<CategoryTotal>
}