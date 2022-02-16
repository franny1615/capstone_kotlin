package com.example.aem.Transactions

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R

class SumTransactionsDialogFragment(val itemId: String) : DialogFragment() {
    private var mappingWithTranId = mutableMapOf<String, ArrayList<Long>>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val transactionsVM =  ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val categoryTotals = createMapping(transactionsVM.getAllTransactionsByItemId(itemId))
        val categoryStringArray : Array<CharSequence> = createArrayForDialog(categoryTotals).toTypedArray()
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.sumDialogTitle)
            builder.setMultiChoiceItems(categoryStringArray, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(which)
                }
            }
            // Set the action buttons
            builder.setPositiveButton(R.string.ok) { dialog, _ ->
                addAllSelectedExpenses(selectedItems)
                dialog.dismiss()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createMapping(list : List<TransactionEntity>) : Map<String,Double> {
        val totalsMapping = mutableMapOf<String,Double>()
        for(transaction  in list) {
            if(totalsMapping.containsKey(transaction.category)){
                mappingWithTranId[transaction.category!!]?.add(transaction.tranId)
                totalsMapping[transaction.category!!] = transaction.amount + totalsMapping[transaction.category!!]!!
            } else {
                mappingWithTranId[transaction.category!!] = arrayListOf()
                mappingWithTranId[transaction.category!!]?.add(transaction.tranId)
                totalsMapping[transaction.category!!] = transaction.amount
            }
        }
        return totalsMapping
    }

    private fun createArrayForDialog(totals : Map<String,Double>) : ArrayList<CharSequence> {
        val arrayList = arrayListOf<CharSequence>()
        for(key in totals.keys) {
            val dollarAmount = "%.2f".format(totals[key])
            arrayList.add("$key, $${dollarAmount}")
        }
        return arrayList
    }

    private fun addAllSelectedExpenses(selected : ArrayList<Int>) {
        val expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        val keys = mappingWithTranId.keys.toTypedArray()
        for(index in selected) {
            val selct = mappingWithTranId[keys[index]]
            if (selct != null) {
                for(tranId in selct){
                    val expense = Expense()
                    expense.tranId = tranId
                    expenseViewModel.insertExpense(expense)
                }
            }
        }
    }
}