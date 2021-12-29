package com.example.aem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    fun insertExpense(v:Expense) {
        executorService.execute {expenseDao.insertExpense(v)}
    }

    val allExpenses : List<Expense>?
        get() {
            val f: Future<List<Expense?>?>? = executorService.submit(expenseDao::getAllExpenses)
            return f?.get(200, TimeUnit.MILLISECONDS) as List<Expense>?
        }
}