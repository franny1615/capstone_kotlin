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
}