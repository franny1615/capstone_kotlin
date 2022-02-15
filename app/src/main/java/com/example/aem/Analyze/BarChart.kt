package com.example.aem.Analyze

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class BarChart(val map:Map<String, Double>, val context: Context, val width:Int){
    fun createGraph() : View {
        val barGraph = LinearLayout(context)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        barGraph.layoutParams = params
        barGraph.orientation = LinearLayout.VERTICAL
        //
        barGraph.addView(createTitle())
        //
        for(key in map.keys) {
            barGraph.addView(createBarView(key,map[key]!!))
        }
        return barGraph
    }

    private fun createTitle() : TextView {
        val textView = TextView(context)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.text = "Visualization"
        textView.textSize = 20f
        textView.setTypeface(null, Typeface.BOLD)
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.setTextColor(Color.BLACK)
        textView.layoutParams = params
        return textView
    }

    private fun createBarView(key:String, data:Double) : View {
        val barView = LinearLayout(context)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10,10,10,10)
        barView.layoutParams = params
        barView.orientation = LinearLayout.HORIZONTAL
        barView.addView(createText(key))
        barView.addView(createBar(data))
        if(data < width) {
            barView.addView(createText("$${map[key]!!}"))
        }
        return barView
    }

    private fun createText(key:String) : View {
        val textView = TextView(context)
        val params = LinearLayout.LayoutParams(
            150,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.text = key
        textView.setTextColor(Color.BLACK)
        textView.layoutParams = params
        return textView
    }

    private fun createBar(data:Double) : View {
        val barV = TextView(context)
        val dataAsInt = data.toInt()
        var params = LinearLayout.LayoutParams(
            dataAsInt,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if(dataAsInt > width) {
            params = LinearLayout.LayoutParams(
                width-180,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val t = "$${data} ->"
            barV.setTextColor(Color.WHITE)
            barV.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
            barV.text = t
        }
        barV.layoutParams = params
        barV.setBackgroundColor(Color.BLUE)
        return barV
    }
}