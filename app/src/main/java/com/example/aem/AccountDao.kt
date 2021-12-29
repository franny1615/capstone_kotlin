package com.example.aem

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBA(ba: AccountEntity): Long

    @Update
    fun updateBA(ba: AccountEntity): Int

    @Delete
    fun deleteBA(ba: AccountEntity): Int

    @Query("SELECT * FROM bank_account_table")
    fun loadAllBA(): LiveData<List<AccountEntity>>

    @Query("SELECT * FROM bank_account_table")
    fun loadAllBARaw(): List<AccountEntity>
}