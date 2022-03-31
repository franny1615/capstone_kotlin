package com.example.aem.Analyze

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aem.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MakeBarChart(
    private val layoutView: View,
    private val chartId: Int,
    private val values: List<Float>,
    private val labels: List<String>,
    private val legendLabel: String,
    private val width: Int,
    private val chartTitle: String
) {
    private var barChart: BarChart = if (chartId == -1) {
        BarChart(layoutView.context)
    } else {
        layoutView.findViewById(chartId)
    }
    private var legend = barChart.legend

    init {
        configureBarChart()
        configureLegend()
        applyData()
    }

    private fun configureBarChart() {
        barChart.setDrawBarShadow(true)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.setMaxVisibleValueCount(60)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)
    }

    private fun configureLegend() {
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.xEntrySpace = 4f
    }

    private fun applyData() {
        val barEntries = arrayListOf<BarEntry>()
        var i = 0f
        for (v in values) {
            barEntries.add(BarEntry(i, v))
            i++
        }
        //
        val barDataSet = BarDataSet(barEntries, legendLabel)
        barDataSet.color = Color.BLUE
        barDataSet.setDrawIcons(false)
        val barData = BarData(barDataSet)
        barData.setValueTextSize(10f)
        barData.barWidth = 0.8f
        //
        barChart.xAxis.labelCount = labels.size
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.data = barData
        if (chartId == -1) {
            val linView = LinearLayout(layoutView.context)
            linView.orientation = LinearLayout.VERTICAL
            linView.layoutParams = LinearLayout.LayoutParams(
                width,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            //
            barChart.layoutParams = LinearLayout.LayoutParams(
                width,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            //
            val title = TextView(layoutView.context)
            title.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            title.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            title.text = chartTitle
            //
            linView.addView(title)
            linView.addView(barChart)
            val lin = (layoutView as LinearLayout)
            lin.addView(linView)
        }
    }
}