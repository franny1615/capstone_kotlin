package com.example.aem.Transactions

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionAdapter(var dataSet: ArrayList<TransactionEntity>, var activityFrom: String, private val expenseViewModel: ExpenseViewModel) : RecyclerView.Adapter<TransactionViewHolder>() {
    private lateinit var filteredList : ArrayList<TransactionEntity>
    private var copyOfDataSet = dataSet
    private var setCategory = ""
    private var setDate = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TransactionViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.transactions_cardview, viewGroup, false)
        return TransactionViewHolder(view,activityFrom,this, expenseViewModel)
    }

    fun setData(dataSet: ArrayList<TransactionEntity>){
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: TransactionViewHolder, position: Int) {
        val amnt = "$${dataSet[position].amount}"
        viewHolder.transactionTitle.text = dataSet[position].merchant
        viewHolder.amount.text = amnt
        viewHolder.category.text = dataSet[position].category
        val date = LocalDate.parse(dataSet[position].date, DateTimeFormatter.ISO_LOCAL_DATE)
        val dateFormatter = DateTimeFormatter.ofPattern("EEE, LLL dd, yyyy")
        val formatted = date.format(dateFormatter)
        viewHolder.date.text = formatted
        viewHolder.itemId = dataSet[position].itemId
        viewHolder.tranId = dataSet[position].tranId
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun filterByDate(startDate : LocalDate, endDate: LocalDate) {
        filteredList = arrayListOf()
        // start >= transdate <= enddate
        for(transaction in copyOfDataSet){
            val transDate = LocalDate.parse(transaction.date, DateTimeFormatter.ISO_LOCAL_DATE)
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
                if(transaction.category == category) {
                    filteredList.add(transaction)
                }
            }
            setData(filteredList)
        }
    }
}