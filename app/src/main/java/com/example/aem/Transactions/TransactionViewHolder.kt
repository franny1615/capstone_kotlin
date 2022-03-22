package com.example.aem.Transactions

import android.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R

class TransactionViewHolder(view : View, activityFrom: String, private val adapter: TransactionAdapter, private val expenseViewModel: ExpenseViewModel) : RecyclerView.ViewHolder(view) {
    val transactionTitle: TextView = view.findViewById(R.id.transaction_title)
    val amount: TextView = view.findViewById(R.id.transaction_amount)
    val category: TextView = view.findViewById(R.id.transaction_category)
    val date: TextView = view.findViewById(R.id.transaction_date)
    var itemId = ""
    var tranId = 0L
    val layout: LinearLayout = view.findViewById(R.id.transaction_linearlayout)
    init {
        var message = ""
        var positiveButton = ""
        when(activityFrom) {
            "transaction" -> {
                message = "Save as expense?"
                positiveButton = "Save"
            }
            "expenses" -> {
                message = "Remove from expenses?"
                positiveButton = "Remove"
            }
        }
        //
        val alertDialog: AlertDialog = view.context.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
            builder.apply {
                setPositiveButton(positiveButton) { dialog,_ ->
                    when(activityFrom) {
                        "transaction" -> doWhenIsTransactionActivity()
                        "expenses" -> doWhenIsExpenseActivity()
                    }
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder.create()
        }
        //
        layout.setOnClickListener {
            alertDialog.show()
        }
    }

    private fun doWhenIsTransactionActivity() {
        expenseViewModel.insertExpense(Expense(tranId))
    }

    private fun doWhenIsExpenseActivity() {
        expenseViewModel.deleteExpenseByTransactionId(tranId.toString())
        var i = 0
        for(transaction in adapter.dataSet) {
            if(transaction.tranId == tranId) {
                break
            }
            i++
        }
        adapter.dataSet.removeAt(i)
        adapter.notifyDataSetChanged()
    }
}