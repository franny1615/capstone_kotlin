package com.example.aem.Expense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.aem.Accounts.AccountEntity
import com.example.aem.AppDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val expenseDao : ExpenseDao
    private val executorService: ExecutorService

    init {
        val db : AppDatabase? = AppDatabase.getInstance(application)
        expenseDao = db!!.expenseDao()
        executorService = Executors.newSingleThreadExecutor()
    }

    fun insertExpense(v: Expense) {
        var exists = false
        if(allExpenses != null) {
            for(expense in allExpenses!!) {
                if(expense.tranId == v.tranId) {
                    exists = true
                }
            }
        }
        if(!exists) {
            executorService.execute {expenseDao.insertExpense(v)}
        }
    }

    fun deleteExpenseByTransactionId(id: String) {
        executorService.execute {expenseDao.deleteExpenseByTranId(id)}
    }

    val allExpensesLiveData: LiveData<List<Expense>>
        get() = expenseDao.getAllExpensesLiveData()

    val allExpenses : List<Expense>?
        get() {
            val f: Future<List<Expense?>?>? = executorService.submit(expenseDao::getAllExpenses)
            return f?.get(200, TimeUnit.MILLISECONDS) as List<Expense>?
        }
}