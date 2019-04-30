package lab1

import distribution.LinearCongruenceUniformDistribution
import distribution.SimpleCongruencesUniformDistribution
import distribution.UniformDistribution
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper


fun main() {

    val columns = 20

    val distributions = mapOf(
        "Встроенный рандом" to UniformDistribution(0.0, 1.0),
        "Простые конгруэнции" to SimpleCongruencesUniformDistribution(0.0, 1.0),
        "Линейная конгруэнтная последовательность" to LinearCongruenceUniformDistribution(0.0, 1.0)
    )

    val getHistoPoint =
        { value: Double, step: Double -> ((value / step).toInt() * step) }

    val charts = distributions.map { entry ->
        val name = entry.key
        val distribution = entry.value

        val step = distribution.length / columns

        val numberList = Array(100) { distribution.getNextNumber() }

        val valMap = sortedMapOf<Double, Int>().apply {
            (0 until columns).forEach {
                put(it.toDouble() * step, 0)
            }
        }

        numberList.forEach { valMap.merge(getHistoPoint(it, step), 1, Integer::sum) }

        getDistChart(valMap, name)
    }

    SwingWrapper(charts).displayChartMatrix()

}

fun getDistChart(valMap: MutableMap<Double, Int>, name: String): CategoryChart {
    val chart = CategoryChartBuilder().title(name).build()
    chart.styler.setHasAnnotations(false)

    chart.addSeries("Плотность распределения", valMap.keys.toDoubleArray(),
        valMap.values.map { it.toDouble() }.toDoubleArray())

    return chart
}


