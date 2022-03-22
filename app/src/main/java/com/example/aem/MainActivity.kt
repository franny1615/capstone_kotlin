package com.example.aem

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.aem.Accounts.AccountViewModel
import com.example.aem.Accounts.AccountsFragment
import com.example.aem.Analyze.AnalyzeFragment
import com.example.aem.Expense.ExpenseFragment
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.Transactions.TransactionFragment
import com.example.aem.Transactions.TransactionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // the following piece gets rid of status bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // navigation panel logic
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        //
        val accountsViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        val transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        val expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        val accountsFragment = AccountsFragment(accountsViewModel = accountsViewModel, transactionViewModel = transactionViewModel)
        //
        val currentPage = findViewById<TextView>(R.id.page_title_textview)
        currentPage.text = getText(R.string.accounts)
        setCurrentFragment(accountsFragment)
        bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_account_button->{
                    currentPage.text = getText(R.string.accounts)
                    setCurrentFragment(accountsFragment)
                }
                R.id.nav_expense_button->{
                    currentPage.text = getText(R.string.expenses)
                    val expensesFragment = ExpenseFragment(expenseViewModel = expenseViewModel,transactionViewModel = transactionViewModel)
                    setCurrentFragment(expensesFragment)
                }
                R.id.nav_transaction_button->{
                    currentPage.text = getText(R.string.transactions)
                    val transFragment = TransactionFragment(accountViewModel = accountsViewModel, transactionsViewModel = transactionViewModel, expenseViewModel = expenseViewModel)
                    setCurrentFragment(transFragment)
                }
                R.id.nav_visualize_button->{
                    currentPage.text = getText(R.string.analyze)
                    val analyzeFragment = AnalyzeFragment()
                    setCurrentFragment(analyzeFragment)
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }
    }
}