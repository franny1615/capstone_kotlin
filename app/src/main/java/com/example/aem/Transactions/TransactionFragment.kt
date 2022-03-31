package com.example.aem.Transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.example.aem.Accounts.AccountEntity
import com.example.aem.Accounts.AccountViewModel
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.Networking
import com.example.aem.R
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionFragment(
    private val transactionsViewModel: TransactionViewModel,
    private val accountViewModel: AccountViewModel,
    private val expenseViewModel: ExpenseViewModel
) : Fragment() {
    private lateinit var layoutView: View
    private lateinit var loadingCircle: ProgressBar
    private lateinit var accountSelector: Spinner
    private lateinit var itemId: String
    private lateinit var transactionRecyclerView: RecyclerView
    private val activityFrom = "transaction"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.transactions_fragment, container, false)
        loadingCircle = requireActivity().findViewById(R.id.loading_circle)
        //
        transactionRecyclerView = layoutView.findViewById(R.id.transactions_recyclerview)
        // set up spinner
        initSpinner(layoutView)
        return layoutView
    }

    private fun initSpinner(layoutView: View) {
        accountSelector = layoutView.findViewById(R.id.account_selection)
        val accounts = accountViewModel.allAccountsRaw
        val accountAdapter =
            ArrayAdapter<String>(layoutView.context, android.R.layout.simple_spinner_dropdown_item)
        if (accounts != null && accounts.isNotEmpty()) {
            accountAdapter.add("Select Account")
            accounts.forEach {
                accountAdapter.add(it.institution)
            }
            accountSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        loadingCircle.visibility = ProgressBar.VISIBLE
                        setListItems(position - 1, accounts, accounts[position - 1].itemId)
                    } else {
                        layoutView.findViewById<RecyclerView>(R.id.transactions_recyclerview).adapter =
                            TransactionAdapter(ArrayList(0), activityFrom, expenseViewModel)
                    }
                }
            }
            accountSelector.adapter = accountAdapter
            accountSelector.setSelection(1)
        } else {
            accountAdapter.add("No accounts present...")
            accountSelector.adapter = accountAdapter
        }
    }

    private fun setListItems(position: Int, accounts: List<AccountEntity>, itId: String) {
        itemId = itId
        val accessToken = accounts[position].accessToken
        val trans = transactionsViewModel.getAllTransactionsByItemId(itemId)
        val transArr = arrayListOf<TransactionEntity>()
        transArr.addAll(trans)
        if (trans.isNotEmpty()) {
            transactionRecyclerView.adapter =
                TransactionAdapter(transArr, activityFrom, expenseViewModel)
            loadingCircle.visibility = ProgressBar.INVISIBLE
        } else {
            val params = HashMap<String, String>(2)
            params["item_id"] = itId
            params["access_token"] = accessToken
            Networking().runInternetRequest(
                "https://cap-backe.herokuapp.com/api/transactions",
                params,
                ::insertTransactions,
                layoutView.context,
                Request.Method.POST
            )
        }
    }

    private fun insertTransactions(resp: JSONObject) {
        val transactions = resp.getJSONArray("transactions")
        val transactionArray = ArrayList<TransactionEntity>()
        for (i in 0 until transactions.length()) {
            val transaction = transactions.getJSONObject(i)
            val date =
                LocalDate.parse(transaction.getString("date"), DateTimeFormatter.RFC_1123_DATE_TIME)
            val trans = TransactionEntity(
                itemId,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                transaction.getJSONArray("category")[0].toString(),
                transaction.getString("name"),
                transaction.getString("amount").toDouble()
            )
            transactionsViewModel.insertTransaction(trans)
            transactionArray.add(trans)
        }
        transactionRecyclerView.adapter =
            TransactionAdapter(transactionArray, activityFrom, expenseViewModel)
        SumTransactionsDialogFragment(
            itemId,
            transactionsViewModel,
            expenseViewModel
        ).show(this.parentFragmentManager, "CategoryTotals")
        loadingCircle.visibility = ProgressBar.INVISIBLE
    }
}