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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        //
        layoutView.findViewById<ExtendedFloatingActionButton>(R.id.date_picker).setOnClickListener {
            showDateRangePicker()
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

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val picker = builder.build()
        // TODO
        // you can make a validator to limit dates that can be picked
        // stackoverflow: https://stackoverflow.com/questions/66988040/limit-to-max-7-days-selection-in-material-date-range-picker-android
        picker.addOnCancelListener {
            picker.dismiss()
        }
        picker.addOnNegativeButtonClickListener{
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            // TODO
            // it.first to it.second is the date range
            // see if you can limit the date range by finding out the max and min of the date
            picker.dismiss()
        }
        picker.show(this.parentFragmentManager,"date range picker")
    }
}