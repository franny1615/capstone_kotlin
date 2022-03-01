package com.example.aem.Transactions

import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Transaction
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionAdapter(var dataSet: ArrayList<TransactionEntity>, var activityFrom: String) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private lateinit var filteredList : ArrayList<TransactionEntity>
    private var copyOfDataSet = dataSet
    private var setCategory = ""
    private var setDate = false

    class ViewHolder(view : View, activityFrom: String, val adapter: TransactionAdapter) : RecyclerView.ViewHolder(view) {
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
            val expense = Expense()
            expense.tranId = tranId.text.toString().toLong()
            expenseViewModel.insertExpense(expense)
        }

        private fun doWhenIsExpenseActivity() {
            expenseViewModel.deleteExpenseByTransactionId(tranId.text.toString())
            var i = 0
            for(transaction in adapter.dataSet) {
                if(transaction.tranId == tranId.text.toString().toLong()) {
                   break
                }
                i++
            }
            adapter.dataSet.removeAt(i)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.transactions_cardview, viewGroup, false)
        return ViewHolder(view,activityFrom,this)
    }

    fun setData(dataSet: ArrayList<TransactionEntity>){
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val amnt = "$${dataSet.get(position).amount}"
        viewHolder.transactionTitle.text = dataSet[position].merchant
        viewHolder.amount.text = amnt
        viewHolder.category.text = dataSet[position].category
        val date = LocalDate.parse(dataSet[position].date!!, DateTimeFormatter.ISO_LOCAL_DATE)
        val dateFormatter = DateTimeFormatter.ofPattern("EEE, LLL dd, yyyy")
        val formatted = date.format(dateFormatter)
        viewHolder.date.text = formatted
        viewHolder.itemId.text = dataSet[position].itemId
        viewHolder.tranId.text = dataSet[position].tranId.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun filterByDate(startDate : LocalDate, endDate: LocalDate) {
        filteredList = arrayListOf()
        // start >= transdate <= enddate
        for(transaction in copyOfDataSet){
            val transDate = LocalDate.parse(transaction.date!!, DateTimeFormatter.ISO_LOCAL_DATE)
            val goodFromBelow = transDate.isAfter(startDate) || transDate.isEqual(startDate)
            val goodFromAbove = transDate.isBefore(endDate) || transDate.isEqual(endDate)
            if(goodFromAbove && goodFromBelow) {
                if(setCategory == "") {
                    filteredList.add(transaction)
                } else if(transaction.category == setCategory) {
                    filteredList.add(transaction)
                }
            }
        }
        setDate = true
        setData(filteredList)
    }

    fun reset() {
        setCategory = ""
        setDate = false
        setData(copyOfDataSet)
    }

    fun filterByCategory(category : String) {
        if(setDate) {
            val list = arrayListOf<TransactionEntity>()
            for(transaction in filteredList) {
                if(transaction.category == category) {
                    list.add(transaction)
                }
            }
            setData(list)
        } else {
            filteredList = arrayListOf()
            setCategory = category
            for(transaction in copyOfDataSet) {
                if(transaction.category!! == category) {
                    filteredList.add(transaction)
                }
            }
            setData(filteredList)
        }
    }
}