package lab1

import distribution.LinearCongruenceUniformDistribution
import distribution.SimpleCongruencesUniformDistribution
import distribution.UniformDistribution
import org.knowm.xchart.*
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.style.markers.SeriesMarkers


fun main() {

    val columns = 20

    val distributions = mapOf(
//        "Встроенный генератор" to UniformDistribution(0.0, 1.0),
        "Простые конгруэнции" to SimpleCongruencesUniformDistribution(0.0, 1.0)
//        "Линейная конгруэнтная последовательность" to LinearCongruenceUniformDistribution(0.0, 1.0)
    )

    val getHistoPoint =
        { value: Double, step: Double -> ((value / step).toInt() * step) }

    val charts = distributions.map { entry ->
        val name = entry.key
        val distribution = entry.value

        val step = distribution.length / columns

        val numberList = Array(1_000_000) { distribution.getNextNumber() }

        val valMap = sortedMapOf<Double, Int>().apply {
            (0 until columns).forEach {
                put(it.toDouble() * step, 0)
            }
        }

        numberList.forEach { valMap.merge(getHistoPoint(it, step), 1, Integer::sum) }

        getDistChart(valMap, name)
    }

//    SwingWrapper(charts).displayChartMatrix()
    charts.forEach {
        SwingWrapper(it).displayChart()
    }

}

fun getDistChart(valMap: MutableMap<Double, Int>, name: String): XYChart {
    val chart = XYChartBuilder().title(name).build()
    chart.styler.setHasAnnotations(false)

    val series = chart.addSeries("Плотность распределения", valMap.keys.toDoubleArray(),
        valMap.values.map { it.toDouble() }.toDoubleArray())

    series.marker = SeriesMarkers.NONE
    chart.styler.defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.StepArea
//    chart.styler.isXAxisLogarithmic = true

    return chart
}


