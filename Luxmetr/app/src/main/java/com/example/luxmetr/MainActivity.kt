package com.example.luxmetr


import android.graphics.Color
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mLux = 0.0f
    private lateinit var mSensorManager: SensorManager
    private lateinit var mLightSensor: Sensor
    private lateinit var yVals: ArrayList<Entry>

    private var currentTime: Long = 0
    private var savedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChart()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread { updateData(mLux, 0) }
            }
        }, 0, 1000)

    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(
            this, mLightSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    override fun onPause() {
        mSensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            mLux = event.values[0]
            imageView.background.alpha = 0
            if (mLux > 150) {
                imageView.background.alpha = 255
            } else {
                imageView.background.alpha = (((255 - 50) * (mLux.toInt() - 0)) / (150 - 0) + 50)
            }
        }
        lightValue.text = java.lang.String.valueOf(mLux.toInt() )
    }

    private fun updateData(value: Float, time: Long) {
        if (chart == null) {
            return
        }
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            val set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = yVals
            val entry = Entry(savedTime.toFloat(), value)
            set1.addEntry(entry)
            if (set1.entryCount > 50) {
                set1.removeFirst()
                set1.setDrawFilled(true)
            }
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
            savedTime++
        }
    }

    private fun initChart() {

        currentTime = Date().time
        chart.setViewPortOffsets(50F, 20F, 5F, 60F)
        chart.description = null
        chart.setTouchEnabled(false)
        // Если отключено, то по x и y значения формируется отдельно друг от друга
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        chart.axisRight.isEnabled = true

        val x: XAxis = chart.xAxis
        x.setLabelCount(8, false)
        x.isEnabled = true
        x.textColor = Color.LTGRAY
        x.textSize = 12F
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(true)
        x.axisLineColor = Color.LTGRAY
        val y: YAxis = chart.getAxisLeft()
        y.setLabelCount(6, true)
        y.textColor = Color.LTGRAY
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        y.setAxisMinValue(0f);
        y.textSize = 12F
        y.setDrawGridLines(false)
        y.axisLineColor = Color.LTGRAY


        yVals = ArrayList()
        yVals.add(Entry(0.toFloat(), 0.toFloat()))

        val set1 = LineDataSet(yVals, "DataSet 1")
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set1.cubicIntensity = 0.02f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.setCircleColor(Color.RED)
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.color = Color.rgb(153, 102, 255)
        set1.fillColor = Color.GRAY
        set1.fillAlpha = 60
        set1.setDrawHorizontalHighlightIndicator(true)
        set1.fillFormatter = IFillFormatter { _, _ -> (-10).toFloat() }

        val data: LineData
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            data = chart.lineData
            data.clearValues()
            data.removeDataSet(0)
            data.addDataSet(set1)
        } else {
            data = LineData(set1)
        }
        data.setValueTextSize(9f)
        data.setDrawValues(false)
        chart.data = data
        chart.legend.isEnabled = false
        chart.animateXY(100, 100)
        chart.invalidate()
    }
}