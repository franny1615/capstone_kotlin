package com.example.aem

import android.content.Context
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Networking() {
    fun runInternetRequest(URL:String, params:HashMap<String,String>, handleResponse: (JSONObject) -> Unit, context: Context, type:Int) {
        val queue = Volley.newRequestQueue(context)
        val request: StringRequest = object : StringRequest(
            type,
            URL,
            Response.Listener { response -> handleResponse(JSONObject(response)) },
            Response.ErrorListener { obj: VolleyError -> obj.printStackTrace() })
        {
            override fun getParams(): Map<String, String> { return params }
        }
        queue.add(request)
    }
}