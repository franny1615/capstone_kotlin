package com.example.aem.Analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R
import com.example.aem.Transactions.TransactionEntity
import com.example.aem.Transactions.TransactionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AnalyzeFragment(private val expenseVM: ExpenseViewModel, private val transVM : TransactionViewModel) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val layoutView = inflater.inflate(R.layout.analyze_fragment,container,false)
        val allExpenses = expenseVM.allExpensesAsTransactions
        if(allExpenses.isNotEmpty()) {
            displayTotalTextView(layoutView,allExpenses)
            displayCategoryTotalChart(layoutView)
            displayMonthlyCharts(layoutView)
        }
        return layoutView
    }

    private fun displayTotalTextView(layoutView: View,allExpenses:List<TransactionEntity>) {
        val totalTextView = layoutView.findViewById<TextView>(R.id.total_expense_textview)
        val totText = "$${"%.2f".format(TotalsComponent().getTotal(allExpenses))}"
        totalTextView.text = totText
    }

    private fun displayCategoryTotalChart(layoutView: View) {
        val categoryTotal = transVM.entireListCategoryTotals
        val values = arrayListOf<Float>()
        val labels = arrayListOf<String>()
        for(category in categoryTotal) {
            values.add(category.amount.toFloat())
            labels.add(category.category.substringBefore(" "))
        }
        MakeBarChart(layoutView,R.id.chart1,values,labels,"Category Total",-1,"")
    }

    private fun displayMonthlyCharts(layoutView: View) {
        val monthlyLayout = layoutView.findViewById<LinearLayout>(R.id.monthly_charts)
        monthlyLayout.isScrollContainer = true
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for(i in 0..23) {
            val current = LocalDateTime.now().minusMonths(i.toLong())
            val currentMinusThirty = current.minusDays(30)
            // relevant strings for bar chart setup
            val currentMonth = current.month.toString()
            val currentYear = current.year
            val barTitle = "$currentMonth $currentYear"
            // parameters for sql query
            val end = current.format(formatter)
            val start = currentMinusThirty.format(formatter)
            // FORMAT: YYYY-MM-DD
            val categories = transVM.getCategoryTotalsInTimeFrame(start,end)
            //
            val values = arrayListOf<Float>()
            val labels = arrayListOf<String>()
            for(cat in categories) {
                values.add(cat.amount.toFloat())
                labels.add(cat.category)
            }
            MakeBarChart(monthlyLayout,-1,values,labels,"Category Total",resources.displayMetrics.widthPixels,barTitle)
        }
    }
}