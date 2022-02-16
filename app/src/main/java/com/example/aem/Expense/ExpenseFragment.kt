package com.example.aem.Expense

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R
import com.example.aem.Transactions.TransactionAdapter
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ExpenseFragment: Fragment() {
    private lateinit var layoutView: View
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private val activityFrom = "expenses"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        layoutView.findViewById<FloatingActionButton>(R.id.date_picker).setOnClickListener {
            showDateRangePicker()
        }
        //
        layoutView.findViewById<FloatingActionButton>(R.id.reset).setOnClickListener {
            (layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter as TransactionAdapter).reset()
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
        return transactionsExpensed.sortedByDescending { it.date }
    }

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val constrainBuilder = CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.before(MaterialDatePicker.todayInUtcMilliseconds()))
        builder.setCalendarConstraints(constrainBuilder.build())
        val picker = builder.build()
        picker.addOnCancelListener {
            picker.dismiss()
        }
        picker.addOnNegativeButtonClickListener{
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            val startDate = Instant.ofEpochMilli(it.first).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = Instant.ofEpochMilli(it.second).atZone(ZoneId.systemDefault()).toLocalDate()
            val adapter: TransactionAdapter = layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter as TransactionAdapter
            adapter.filter(startDate,endDate)
            picker.dismiss()
        }
        picker.show(this.parentFragmentManager,"date range picker")
    }
}