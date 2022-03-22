package com.example.aem.Accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R
import com.example.aem.Transactions.TransactionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AccountsAdapter(private var dataSet: List<AccountEntity>, private var accountViewModel: AccountViewModel, private var transactionViewModel: TransactionViewModel) :
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val institution: TextView = view.findViewById(R.id.account_institution_textview)
        val accountId: TextView = view.findViewById(R.id.account_id_textview)
        val delete: FloatingActionButton = view.findViewById(R.id.delete_button)
    }

    fun setData(newData: List<AccountEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.accounts_cardview, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.institution.text = dataSet[position].institution
        val id = "Id:" + dataSet[position].id
        viewHolder.accountId.text = id
        viewHolder.delete.setOnClickListener {
            accountViewModel.deleteBankAccount(dataSet[position])
            transactionViewModel.deleteAllTransactionsByItemId(dataSet[position].itemId)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
