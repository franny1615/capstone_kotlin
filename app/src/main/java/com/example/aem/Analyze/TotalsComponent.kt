package com.example.aem.Analyze

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.aem.R
import com.example.aem.Transactions.TransactionEntity

class TotalsComponent() {
    fun getTotal(list : List<TransactionEntity>) : Double {
        var total = 0.0
        for(transaction  in list) {
            total += transaction.amount
        }
        return total
    }
}