package com.example.aem.Accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.aem.AppDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val accountDao: AccountDao
    private val executorService: ExecutorService

    fun insertBankAccount(ba: AccountEntity) {
        executorService.execute { accountDao.insertBA(ba) }
    }

    fun updateBankAccount(ba: AccountEntity) {
        executorService.execute { accountDao.updateBA(ba) }
    }

    fun deleteBankAccount(ba: AccountEntity) {
        executorService.execute { accountDao.deleteBA(ba) }
    }

    val allAccounts: LiveData<List<AccountEntity>>
        get() = accountDao.loadAllBA()

    val allAccountsRaw: List<AccountEntity>?
        get() {
            val f: Future<List<AccountEntity?>?>? = executorService.submit(accountDao::loadAllBARaw)
            return f?.get(200, TimeUnit.MILLISECONDS) as List<AccountEntity>?
        }

    init {
        val db: AppDatabase? = AppDatabase.getInstance(application)
        accountDao = db!!.bankAccountDao()
        executorService = Executors.newSingleThreadExecutor()
    }
}