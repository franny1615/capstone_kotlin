package com.example.aem.Transactions

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R

class SumTransactionsDialogFragment(private val itemId: String, private val transactionViewModel: TransactionViewModel, private val expenseViewModel: ExpenseViewModel) : DialogFragment() {
    private val categoryMap = mutableMapOf<String,ArrayList<TransactionEntity>>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.sumDialogTitle)
            builder.setMultiChoiceItems(createCategoryCharSequence().toTypedArray(), null) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(which)
                }
            }
            // Set the action buttons
            builder.setPositiveButton(R.string.ok) { dialog, _ ->
                addSelectedExpenses(selectedItems)
                dialog.dismiss()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createCategoryCharSequence() : ArrayList<CharSequence> {
        var categoryTotals = transactionViewModel.getCategoryTotals(itemId)
        var tries = 0
        while(categoryTotals.isEmpty() && (tries < 100)) {
            categoryTotals = transactionViewModel.getCategoryTotals(itemId)
            tries++
        }
        val arrayVersion = arrayListOf<CharSequence>()
        for(category in categoryTotals) {
            val roundedAmount = "%.2f".format(category.amount)
            arrayVersion.add("${category.category}, $${roundedAmount}")
            categoryMap[category.category] = transactionViewModel.getTransByCatAndItemId(itemId,category.category)
        }
        return arrayVersion
    }

    private fun addSelectedExpenses(selected : ArrayList<Int>) {
        val keys = categoryMap.keys
        for(index in selected) {
            val selct = categoryMap[keys.elementAt(index)]
            if (selct != null) {
                for(transaction in selct){
                    expenseViewModel.insertExpense(Expense(transaction.tranId))
                }
            }
        }
    }
}