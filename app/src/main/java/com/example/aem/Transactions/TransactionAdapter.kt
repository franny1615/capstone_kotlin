package com.example.aem.Transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R

class TransactionAdapter(var dataSet: List<TransactionEntity>)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private lateinit var transactionsViewModel: TransactionViewModel

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val transactionTitle: TextView
        val amount: TextView
        val category: TextView
        val date: TextView
        val id: TextView
        init {
            transactionTitle = view.findViewById(R.id.transaction_title)
            amount = view.findViewById(R.id.transaction_amount)
            category = view.findViewById(R.id.transaction_category)
            date = view.findViewById(R.id.transaction_date)
            id = view.findViewById(R.id.transaction_id)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.transactions_cardview, viewGroup, false)
        transactionsViewModel = ViewModelProvider((view.context as FragmentActivity?)!!).get(
            TransactionViewModel::class.java)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val amnt = "$${dataSet.get(position).amount}"
        viewHolder.transactionTitle.text = dataSet.get(position).merchant
        viewHolder.amount.text = amnt
        viewHolder.category.text = dataSet.get(position).category
        viewHolder.date.text = dataSet.get(position).date
        viewHolder.id.text = dataSet.get(position).itemId
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}