package com.example.aem.Accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aem.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plaid.link.OpenPlaidLink
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResult
import com.plaid.link.result.LinkSuccess
import org.json.JSONObject
import kotlin.collections.HashMap

class AccountsFragment: Fragment() {
    private lateinit var layoutView: View
    private val URL = "https://cap-backe.herokuapp.com"
    private lateinit var accountsContext:Context
    private lateinit var loadingCircle: ProgressBar
    private lateinit var institutionName: String
    private lateinit var accountsViewModel: AccountViewModel

    private val plaidPopUpActivity = registerForActivityResult( OpenPlaidLink() ) { result: LinkResult ->
        if (result is LinkSuccess) {
            val (publicToken, metadata) = result
            institutionName = metadata.institution!!.name
            // came back from plaid api, save new token
            loadingCircle.visibility = ProgressBar.VISIBLE
            val params = HashMap<String,String>(1)
            params.put("public_token",publicToken)
            runInternetRequest(URL+"/api/set_access_token",params,::exchangedPublicToken,accountsContext)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        layoutView = inflater.inflate(R.layout.accounts_fragment,container,false)
        loadingCircle = requireActivity().findViewById(R.id.loading_circle)
        accountsContext = layoutView.context
        // set button to initate first internet request
        layoutView.findViewById<FloatingActionButton>(R.id.accounts_add_button).setOnClickListener {
            loadingCircle.visibility = ProgressBar.VISIBLE
            runInternetRequest(URL+"/api/create_link_token",HashMap(),::createLinkToken,accountsContext)
        }
        //
        accountsViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        if (accountsViewModel.allAccountsRaw != null) {
            val accountAdapter = AccountsAdapter(accountsViewModel.allAccountsRaw!!)
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
        return layoutView
    }

    private fun runInternetRequest(endpoint:String, params:HashMap<String,String>, handleResponse: (JSONObject) -> Unit, context: Context) {
        val queue = Volley.newRequestQueue(context)
        val request: StringRequest = object : StringRequest(
            Method.POST,
            endpoint,
            Response.Listener { response -> handleResponse(JSONObject(response)) },
            Response.ErrorListener { obj: VolleyError -> obj.printStackTrace() })
        {
            override fun getParams(): Map<String, String> { return params }
        }
        queue.add(request)
    }

    private fun createLinkToken(resp: JSONObject) {
        val config = LinkTokenConfiguration.Builder().token(resp.getString("link_token")).build()
        plaidPopUpActivity.launch(config)
        loadingCircle.visibility = ProgressBar.INVISIBLE
    }

    private fun exchangedPublicToken(resp: JSONObject) {
        val newAccount = AccountEntity()
        newAccount.institution = institutionName
        newAccount.itemId = resp.getString("item_id")
        newAccount.accessToken = resp.getString("access_token")
        accountsViewModel.insertBankAccount(newAccount)
        loadingCircle.visibility = ProgressBar.INVISIBLE
    }
}