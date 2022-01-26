package com.example.aem.Expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R
import com.example.aem.Transactions.TransactionAdapter
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionViewModel

class ExpenseFragment: Fragment() {
    private lateinit var layoutView: View
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private val activityFrom = "expenses"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.expense_fragment,container,false)
        expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val expenses = getTransactionsExpensed()
        if(expenses.isNotEmpty()){
            val adapter = TransactionAdapter(expenses,activityFrom)
            expenseViewModel.allExpensesLiveData.observe(viewLifecycleOwner) {
                adapter.setData(getTransactionsExpensed())
            }
            layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter = adapter
        }
        return layoutView
    }

    private fun getTransactionsExpensed() : List<TransactionEntity> {
        val expenses = expenseViewModel.allExpenses
        val transactionsExpensed = arrayListOf<TransactionEntity>()
        if(expenses != null && expenses.isNotEmpty()) {
            for (expense in expenses) {
                transactionsExpensed.add(transactionViewModel.getbyTransactionId(expense.tranId.toString()))
            }
        }
        return transactionsExpensed
    }
}