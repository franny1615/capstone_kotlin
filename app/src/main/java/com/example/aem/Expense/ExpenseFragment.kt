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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.expense_fragment,container,false)
        expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val expenses = expenseViewModel.allExpenses
        if(expenses != null && expenses.isNotEmpty()){
            val transactionsExpensed = arrayListOf(TransactionEntity())
            for(expense in expenses){
                transactionsExpensed.add(transactionViewModel.getbyTransactionId(expense.tranId.toString()))
            }
            // feed it to adapter and set it to recyclerview
            val adapter = TransactionAdapter(transactionsExpensed)
            layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter = adapter
        }
        return layoutView
    }
}