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
            val totalsComponent = TotalsComponent(layoutView)
            val map = totalsComponent.createMapping(transactions)
            totalsComponent.applyViewsToLayout()
            //
            val barGraphLayout = layoutView.findViewById<LinearLayout>(R.id.bargraph_linearlayout)
            val barChart = BarChart(map,layoutView.context,resources.displayMetrics.widthPixels)
            barGraphLayout.addView(barChart.createGraph())
        }
        return layoutView
    }
}