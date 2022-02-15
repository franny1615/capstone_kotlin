package com.example.aem.Analyze

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.aem.R
import com.example.aem.Transactions.TransactionEntity

class TotalsComponent(private val layoutView : View) {
    private var totalsMapping = mutableMapOf<String,Double>()
    private var total = 0.0

    fun createMapping(list : List<TransactionEntity>) : Map<String,Double> {
        for(transaction  in list) {
            total += transaction.amount
            if(totalsMapping.containsKey(transaction.category)){
                totalsMapping[transaction.category!!] = transaction.amount + totalsMapping[transaction.category!!]!!
            } else {
                totalsMapping[transaction.category!!] = transaction.amount
            }
        }
        return totalsMapping
    }

    fun applyViewsToLayout() {
        val totalTextView = layoutView.findViewById<TextView>(R.id.total_expense_textview)
        val totText = "$${"%.2f".format(total)}"
        totalTextView.text = totText
        //
        val categoryTotalLayout = layoutView.findViewById<LinearLayout>(R.id.category_totals_linearlayout)
        for(key in totalsMapping.keys) {
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
            val amount = "$" + "%.2f".format(totalsMapping[key])
            amountTextView.text = amount
            amountTextView.textSize = 20f
            amountTextView.layoutParams = textViewLayoutParams
            amountTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            linearLayout.addView(categoryTextView)
            linearLayout.addView(amountTextView)
            categoryTotalLayout.addView(linearLayout)
        }
    }
}