package com.example.aem.Transactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aem.Accounts.AccountEntity
import com.example.aem.Accounts.AccountViewModel
import com.example.aem.Expense.Expense
import com.example.aem.Expense.ExpenseViewModel
import com.example.aem.R
import org.json.JSONObject

class TransactionFragment : Fragment() {
    private lateinit var layoutView: View
    private lateinit var transactionsViewModel: TransactionViewModel
    private lateinit var loadingCircle: ProgressBar
    private lateinit var accountSelector: Spinner
    private lateinit var itemId: String
    private lateinit var transactionRecyclerView: RecyclerView
    private val activityFrom = "transaction"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.transactions_fragment, container, false)
        loadingCircle = requireActivity().findViewById(R.id.loading_circle)
        //
        transactionRecyclerView = layoutView.findViewById(R.id.transactions_recyclerview)
        transactionsViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        // set up spinner
        initSpinner(layoutView)
        return layoutView
    }

    private fun initSpinner(layoutView: View) {
        accountSelector = layoutView.findViewById(R.id.account_selection)
        val accountViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        val accounts = accountViewModel.allAccountsRaw
        val accountAdapter = ArrayAdapter<String>(layoutView.context, android.R.layout.simple_spinner_dropdown_item)
        accountAdapter.add("Select Account")
        if (accounts != null && accounts.isNotEmpty()) {
            accounts.forEach {
                accountAdapter.add(it.institution)
            }
            accountSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position > 0) {
                        loadingCircle.visibility = ProgressBar.VISIBLE
                        setListItems(position - 1, accounts, accounts[position - 1].itemId.toString())
                    } else {
                        layoutView.findViewById<RecyclerView>(R.id.transactions_recyclerview).adapter = TransactionAdapter(ArrayList(0),activityFrom)
                    }
                }
            }
        }
        accountSelector.adapter = accountAdapter
    }

    private fun setListItems(position: Int, accounts: List<AccountEntity>, itId: String) {
        itemId = itId
        val accessToken = accounts[position].accessToken
        val trans = transactionsViewModel.getAllTransactionsByItemId(itemId)
        if (trans.isNotEmpty()) {
            transactionRecyclerView.adapter = TransactionAdapter(trans,activityFrom)
            loadingCircle.visibility = ProgressBar.INVISIBLE
        } else {
            val params = HashMap<String, String>(2)
            params["item_id"] = itId
            params["access_token"] = accessToken!!
            runInternetRequest("https://cap-backe.herokuapp.com/api/transactions", params, ::insertTransactions, layoutView.context
            )
        }
    }

    private fun runInternetRequest(endpoint: String, params: HashMap<String, String>, handleResponse: (JSONObject) -> Unit, context: Context) {
        val queue = Volley.newRequestQueue(context)
        val request: StringRequest = object : StringRequest(
            Method.POST,
            endpoint,
            Response.Listener { response -> handleResponse(JSONObject(response)) },
            Response.ErrorListener { obj: VolleyError -> obj.printStackTrace() }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(request)
    }

    private fun insertTransactions(resp: JSONObject) {
        val transactions = resp.getJSONArray("transactions")
        for (i in 0 until transactions.length()) {
            val transaction = transactions.getJSONObject(i)
            val trans = TransactionEntity()
            trans.itemId = itemId
            trans.amount = transaction.getString("amount").toDouble()
            trans.date = transaction.getString("date")
            trans.merchant = transaction.getString("name")
            trans.category = transaction.getJSONArray("category").toString()
            transactionsViewModel.insertTransaction(trans)
        }
        loadingCircle.visibility = ProgressBar.INVISIBLE
        transactionRecyclerView.adapter = TransactionAdapter(transactionsViewModel.getAllTransactionsByItemId(itemId),activityFrom)
    }
}