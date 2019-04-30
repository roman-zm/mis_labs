package lab3

import distribution.MullerNormalDistribution
import distribution.NormalDistribution
import distribution.NormalDistributionLab
import lab1.getDistChart
import lab2.round
import org.knowm.xchart.SwingWrapper
import kotlin.math.roundToInt

fun main() {

    val getHistoPoint =
        { value: Double, step: Double -> ((value / step).toInt() * step).round(10) }

    val charts = mapOf(
        "По лабе" to NormalDistributionLab(0.0, 1.0),
        "Другой метод" to NormalDistribution(0.0, 1.0),
        "Метод Мюллера" to MullerNormalDistribution(0.0, 1.0)
    ).map { entry ->
        val distrib = entry.value
        val chartName = entry.key

        val start = distrib.expectedValue - distrib.standardDeviation * 3
        val length = distrib.standardDeviation * 6

        val numberArray = DoubleArray(1000_000) { (distrib.getNextNumber() * 10).roundToInt().toDouble() }

        val columns = 600
        val step = ((numberArray.max()!! - numberArray.min()!!) / columns)


        val valMap = sortedMapOf<Double, Int>()

        numberArray.forEach { valMap.merge(getHistoPoint(it, step), 1, Integer::sum) }

        getDistChart(valMap, chartName)
    }

    SwingWrapper(charts).displayChartMatrix()

}