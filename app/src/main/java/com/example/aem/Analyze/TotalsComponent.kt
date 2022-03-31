package com.example.aem.Analyze

import com.example.aem.Transactions.TransactionEntity

class TotalsComponent() {
    fun getTotal(list: List<TransactionEntity>): Double {
        var total = 0.0
        for (transaction in list) {
            total += transaction.amount
        }
        return total
    }
}