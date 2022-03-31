package com.example.aem.Expense

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R
import com.example.aem.Transactions.TransactionAdapter
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Instant
import java.time.ZoneId

class ExpenseFragment(
    private val expenseViewModel: ExpenseViewModel,
    private val transactionViewModel: TransactionViewModel
) : Fragment() {
    private lateinit var layoutView: View
    private val activityFrom = "expenses"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.expense_fragment, container, false)
        val list = expenseViewModel.allExpensesAsTransactions
        val expenses = arrayListOf<TransactionEntity>()
        expenses.addAll(list)
        if (expenses.isNotEmpty()) {
            val adapter = TransactionAdapter(expenses, activityFrom, expenseViewModel)
            expenseViewModel.allExpensesLiveData.observe(viewLifecycleOwner) {
                adapter.setData(expenses)
            }
            layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter = adapter
            // filter by date
            layoutView.findViewById<FloatingActionButton>(R.id.date_picker).setOnClickListener {
                showDateRangePicker()
            }
            // reset
            layoutView.findViewById<FloatingActionButton>(R.id.reset).setOnClickListener {
                (layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter as TransactionAdapter).reset()
            }
            // filter by category
            layoutView.findViewById<FloatingActionButton>(R.id.category_picker).setOnClickListener {
                CategoryPickerDialogFragment(
                    transactionsViewModel = transactionViewModel,
                    layout = layoutView
                ).show(this.parentFragmentManager, "category picker")
            }
        }
        return layoutView
    }

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val constrainBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(MaterialDatePicker.todayInUtcMilliseconds()))
        builder.setCalendarConstraints(constrainBuilder.build())
        val picker = builder.build()
        picker.addOnCancelListener {
            picker.dismiss()
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            val startDate =
                Instant.ofEpochMilli(it.first).atZone(ZoneId.systemDefault()).toLocalDate()
            var endDate =
                Instant.ofEpochMilli(it.second).atZone(ZoneId.systemDefault()).toLocalDate()
            endDate = endDate.plusDays(1)
            val adapter: TransactionAdapter =
                layoutView.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter as TransactionAdapter
            adapter.filterByDate(startDate, endDate)
            picker.dismiss()
        }
        picker.show(this.parentFragmentManager, "date range picker")
    }
}