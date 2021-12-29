package com.example.aem

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
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
        val accountsFragm = AccountsFragment()
        val transacsFragm = TransactionFragment()
        val currentPage = findViewById<TextView>(R.id.page_title_textview)
        currentPage.text = getText(R.string.accounts)
        setCurrentFragment(accountsFragm)
        bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_account_button->{
                    currentPage.text = getText(R.string.accounts)
                    setCurrentFragment(accountsFragm)
                }
                R.id.nav_expense_button->{
                    currentPage.text = getText(R.string.expenses)
                    setCurrentFragment(accountsFragm)
                }
                R.id.nav_transaction_button->{
                    currentPage.text = getText(R.string.transactions)
                    setCurrentFragment(transacsFragm)
                }
                R.id.nav_visualize_button->{
                    currentPage.text = getText(R.string.analyze)
                    setCurrentFragment(accountsFragm)
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