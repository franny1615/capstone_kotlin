package com.example.aem.Expense

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.aem.R
import com.example.aem.Transactions.TransactionAdapter
import com.example.aem.Transactions.TransactionEntity

class CategoryPickerDialogFragment(val expenses: List<TransactionEntity>, val layout: View) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // categorize expenses
        val categories = arrayListOf<String>()
        for(expense in expenses) {
            if(!categories.contains(expense.category)) {
                categories.add(expense.category!!)
            }
        }
        val catAsCharSeq = Array<CharSequence>(categories.size) { i -> categories[i] }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Filter by Category")
            builder.setItems(catAsCharSeq) { dialog, which ->
                (layout.findViewById<RecyclerView>(R.id.expense_recyclerview).adapter as TransactionAdapter).filterByCategory(categories[which])
                dialog.dismiss()
            }
            // Set the action buttons
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}