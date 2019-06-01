package lab4

import distribution.Distribution
import distribution.ErlangDistribution
import distribution.ExponentialDistribution
import distribution.HyperExponentialDistribution
import lab1.getDistChart
import lab2.round
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.internal.chartpart.Chart
import kotlin.math.roundToInt

fun main() {

    val chartList = listOf(
        prepareChart("Экспоненциальное", ExponentialDistribution(1.5)),
        prepareChart("Гиперэкспоненциальное", HyperExponentialDistribution()),
        prepareChart("Эрланга", ErlangDistribution(10, 0.3))
    )


    SwingWrapper(chartList).displayChartMatrix()
}

fun prepareChart(name: String, distrib: Distribution): Chart<*,*> {
    val getHistoPoint =
        { value: Double, step: Double -> ((value / step).toInt() * step).round(10) }

//    val distrib = ExponentialDistribution(1.5)

    val start = 0.0
    val length = 10.0

    val numberArray = DoubleArray(1_000_000) { (distrib.getNextNumber()) }
        .filter { it in start..length }

    val columns = 60
    val step = ((numberArray.max()!! - numberArray.min()!!) / columns)


    val valMap = sortedMapOf<Double, Int>()

    numberArray.forEach { valMap.merge(getHistoPoint(it, step), 1, Integer::sum) }

    return getDistChart(valMap, name)
}

