package com.example.aem.Accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aem.Networking
import com.example.aem.R
import com.example.aem.Transactions.TransactionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plaid.link.OpenPlaidLink
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResult
import com.plaid.link.result.LinkSuccess
import org.json.JSONObject
import kotlin.collections.HashMap

class AccountsFragment(private val accountsViewModel: AccountViewModel, private val transactionViewModel: TransactionViewModel): Fragment() {
    private lateinit var layoutView: View
    private val url = "https://cap-backe.herokuapp.com"
    private lateinit var loadingCircle: ProgressBar
    private lateinit var institutionName: String
    private lateinit var plaidPopUpActivity: ActivityResultLauncher<LinkTokenConfiguration>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.accounts_fragment,container,false)
        loadingCircle = requireActivity().findViewById(R.id.loading_circle)
        // set button to initiate first internet request
        layoutView.findViewById<FloatingActionButton>(R.id.accounts_add_button).setOnClickListener {
            loadingCircle.visibility = ProgressBar.VISIBLE
            Networking().runInternetRequest("$url/api/create_link_token",HashMap(),::createLinkToken,layoutView.context, Request.Method.POST)
        }
        //
        instantiatePlaidPopUp()
        instantiatePossibleAccountsList()
        return layoutView
    }

    private fun instantiatePossibleAccountsList() {
        if (accountsViewModel.allAccountsRaw != null) {
            val accountAdapter = AccountsAdapter(accountsViewModel.allAccountsRaw!!,accountsViewModel,transactionViewModel)
            val accountRecyclerView = layoutView.findViewById<RecyclerView>(R.id.accounts_recyclerview)
            accountRecyclerView.layoutManager = LinearLayoutManager(layoutView.context)
            accountRecyclerView.adapter = accountAdapter
            accountRecyclerView.setHasFixedSize(true)
            accountsViewModel.allAccounts.observe(viewLifecycleOwner) {
                accountAdapter.setData(
                    accountsViewModel.allAccountsRaw!!
                )
            }
        }
    }

    private fun instantiatePlaidPopUp() {
        plaidPopUpActivity = registerForActivityResult(OpenPlaidLink()) { result: LinkResult ->
            if (result is LinkSuccess) {
                val (publicToken, metadata) = result
                institutionName = metadata.institution!!.name
                // came back from plaid api, save new token
                loadingCircle.visibility = ProgressBar.VISIBLE
                val params = HashMap<String, String>(1)
                params["public_token"] = publicToken
                Networking().runInternetRequest("$url/api/set_access_token",params,::exchangedPublicToken,layoutView.context, Request.Method.POST)
            }
        }
    }

    private fun createLinkToken(resp: JSONObject) {
        val config = LinkTokenConfiguration.Builder().token(resp.getString("link_token")).build()
        plaidPopUpActivity.launch(config)
        loadingCircle.visibility = ProgressBar.INVISIBLE
    }

    private fun exchangedPublicToken(resp: JSONObject) {
        val newAccount = AccountEntity(resp.getString("item_id"),resp.getString("access_token"),institutionName)
        accountsViewModel.insertBankAccount(newAccount)
        loadingCircle.visibility = ProgressBar.INVISIBLE
    }
}