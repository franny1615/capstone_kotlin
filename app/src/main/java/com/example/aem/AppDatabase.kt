package com.example.aem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aem.Accounts.AccountDao
import com.example.aem.Accounts.AccountEntity
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseDao
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionsDao

@Database(
    entities = [AccountEntity::class, TransactionEntity::class, Expense::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // you will populate this only once
    companion object {
        private val DB_NAME = "account_info_db"
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            // basically make the database if it doesn't already exist
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    // open up data access objects through abstract functions
    abstract fun bankAccountDao(): AccountDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun expenseDao(): ExpenseDao
}