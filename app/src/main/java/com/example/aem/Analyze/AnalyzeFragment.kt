package com.example.aem.Analyze

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionViewModel

class AnalyzeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val expenseVM = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        val transacVM = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val layoutView = inflater.inflate(R.layout.analyze_fragment,container,false)
        //
        val allExpenses = expenseVM.allExpenses
        if(allExpenses != null && allExpenses.isNotEmpty()) {
            val transactions = arrayListOf<TransactionEntity>()
            for(expense in allExpenses) {
                transactions.add(transacVM.getbyTransactionId(expense.tranId.toString()))
            }
            //
            var total = 0.0
            val categoryTotalsMap = mutableMapOf<String,Double>()
            val totalTextView = layoutView.findViewById<TextView>(R.id.total_expense_textview)
            for(transaction  in transactions) {
                total += transaction.amount
                if(categoryTotalsMap.containsKey(transaction.category)){
                    categoryTotalsMap[transaction.category!!] = transaction.amount + categoryTotalsMap[transaction.category!!]!!
                } else {
                    categoryTotalsMap[transaction.category!!] = transaction.amount
                }
            }
            val totText = "$$total"
            totalTextView.text = totText
            //
            val categoryTotalLayout = layoutView.findViewById<LinearLayout>(R.id.category_totals_linearlayout)
            for(key in categoryTotalsMap.keys) {
                val linearLayout = LinearLayout(layoutView.context)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val params = LinearLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(10,10,10,10)
                linearLayout.layoutParams = params
                //
                val textViewLayoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
                textViewLayoutParams.weight = 0.5f
                val categoryTextView = TextView(layoutView.context)
                categoryTextView.setTextColor(Color.BLACK)
                categoryTextView.text = key
                categoryTextView.textSize = 20f
                categoryTextView.layoutParams = textViewLayoutParams
                val amountTextView = TextView(layoutView.context)
                val amount = "$" + categoryTotalsMap[key]
                amountTextView.text = amount
                amountTextView.textSize = 20f
                amountTextView.layoutParams = textViewLayoutParams
                amountTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                linearLayout.addView(categoryTextView)
                linearLayout.addView(amountTextView)
                categoryTotalLayout.addView(linearLayout)
            }
        }
        return layoutView
    }
}