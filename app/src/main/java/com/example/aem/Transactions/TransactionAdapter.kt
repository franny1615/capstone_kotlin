package com.example.aem.Transactions

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R

class TransactionAdapter(var dataSet: List<TransactionEntity>, var activityFrom: String) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view : View, activityFrom: String) : RecyclerView.ViewHolder(view) {
        val transactionTitle: TextView = view.findViewById(R.id.transaction_title)
        val amount: TextView = view.findViewById(R.id.transaction_amount)
        val category: TextView = view.findViewById(R.id.transaction_category)
        val date: TextView = view.findViewById(R.id.transaction_date)
        val itemId: TextView = view.findViewById(R.id.item_id)
        val tranId: TextView = view.findViewById(R.id.trans_id)
        val layout: LinearLayout = view.findViewById(R.id.transaction_linearlayout)
        private val expenseViewModel = ViewModelProvider(view.context as FragmentActivity)[ExpenseViewModel::class.java]

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
                            "transaction" -> doWhenIsTransactionActivity(view)
                            "expenses" -> doWhenIsExpenseActivity(view)
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

        private fun doWhenIsTransactionActivity(view: View) {
            val expense = Expense()
            expense.tranId = tranId.text.toString().toLong()
            expenseViewModel.insertExpense(expense)
        }

        private fun doWhenIsExpenseActivity(view: View) {
            expenseViewModel.deleteExpenseByTransactionId(tranId.text.toString())
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.transactions_cardview, viewGroup, false)
        return ViewHolder(view,activityFrom)
    }

    fun setData(dataSet: List<TransactionEntity>){
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val amnt = "$${dataSet.get(position).amount}"
        viewHolder.transactionTitle.text = dataSet.get(position).merchant
        viewHolder.amount.text = amnt
        viewHolder.category.text = dataSet.get(position).category
        viewHolder.date.text = dataSet.get(position).date
        viewHolder.itemId.text = dataSet.get(position).itemId
        viewHolder.tranId.text = dataSet.get(position).tranId.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}