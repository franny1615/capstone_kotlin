package com.example.aem.Transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aem.AppDatabase
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDAO: TransactionsDao
    private val executorService: ExecutorService

    init {
        val db: AppDatabase? = AppDatabase.getInstance(application)
        transactionDAO = db!!.transactionsDao()
        executorService = Executors.newSingleThreadExecutor()
    }

    fun insertTransaction(transaction: TransactionEntity) {
        executorService.execute {
            transactionDAO.insertTransaction(transaction)
        }
    }

    fun deleteAllTransactionsByItemId(itemId: String) {
        executorService.execute {
            transactionDAO.deleteTransactionsByItemId(itemId)
        }
    }

    fun getAllTransactionsByItemId(itemId: String): ArrayList<TransactionEntity> {
        val list = arrayListOf<TransactionEntity>()
        val callable = Callable {
            transactionDAO.loadAllTransactionsByItemId(itemId)
        }
        val f = executorService.submit(callable)
        list.addAll(f.get(500, TimeUnit.MILLISECONDS) as List<TransactionEntity>)
        return list
    }

    fun getSingleTransactionByTranId(transId: String): TransactionEntity {
        val future = executorService.submit {
            transactionDAO.getbyTransactionId(transId)
        }
        return future.get(200, TimeUnit.MILLISECONDS) as TransactionEntity
    }

    fun getCategoryTotals(itemId: String): List<CategoryTotal> {
        val callable = Callable {
            transactionDAO.getCategoryTotals(itemId)
        }
        val f = executorService.submit(callable)
        return f.get(500, TimeUnit.MILLISECONDS)
    }

    fun getEntireListCategoryTotals(): ArrayList<CategoryTotal> {
        val list = arrayListOf<CategoryTotal>()
        val callable = Callable {
            transactionDAO.getEntireCategoryTotals()
        }
        val f = executorService.submit(callable)
        list.addAll(f.get(500, TimeUnit.MILLISECONDS) as List<CategoryTotal>)
        return list
    }

    fun getTransByCatAndItemId(itemId: String, category: String): ArrayList<TransactionEntity> {
        val list = arrayListOf<TransactionEntity>()
        val callable = Callable {
            transactionDAO.getTransactionsBasedOnCategoryAndItemId(itemId, category)
        }
        val f = executorService.submit(callable)
        list.addAll(f.get(500, TimeUnit.MILLISECONDS) as List<TransactionEntity>)
        return list
    }

    fun getCategoryTotalsInTimeFrame(start: String, end: String): List<CategoryTotal> {
        val list = arrayListOf<CategoryTotal>()
        val callable = Callable {
            transactionDAO.getCategoryTotalsInATimeFrame(start, end)
        }
        val f = executorService.submit(callable)
        list.addAll(f.get(500, TimeUnit.MILLISECONDS) as List<CategoryTotal>)
        return list
    }
}