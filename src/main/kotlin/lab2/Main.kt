package lab2

import distribution.TrapezialDistribution
import distribution.TriangleDistribution
import lab1.getDistChart
import org.knowm.xchart.SwingWrapper
import kotlin.math.round

fun main() {

    val getHistoPoint =
        { value: Double, step: Double -> ((value / step).toInt() * step).round(2) }

    val charts = mapOf(
        "Трапецидальное распределение" to TrapezialDistribution(6.0, 14.0, 0.0),
        "Треугольное распределение" to TriangleDistribution(0.0, 20.0)
    ).map { entry ->
        val columns = 40
        val start = entry.value.start
        val step = entry.value.length / columns

        val numberArray = DoubleArray(1_000_000) { entry.value.getNextNumber() }

        val valMap = sortedMapOf<Double, Int>().apply {
            (0 until columns).forEach {
                put((start + it.toDouble() * step).round(2), 0)
            }
        }

        numberArray.forEach { valMap.merge(getHistoPoint(it, step), 1, Integer::sum) }

        getDistChart(valMap, entry.key)
    }

    SwingWrapper(charts).displayChartMatrix()

}


fun Float.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
