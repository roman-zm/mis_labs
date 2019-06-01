import distribution.NormalDistribution
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper
import java.util.*

fun main() {

    val columns = 100

//    val distribution = distribution.UniformDistribution(0.0, 1.0)
    val distribution = NormalDistribution()

//    val step = distribution.length / columns

    val numberList = Array(100000) { distribution.getNextNumber() }

    val step = (numberList.max()!! - numberList.min()!!) / columns

    val valMap = sortedMapOf<Int, Int>().apply {
//        (0 until columns).forEach {
//            put(it, 0)
//        }
    }

    numberList.forEach { valMap.merge((it / step).toInt(), 1, Integer::sum) }

    println(Arrays.toString(numberList))
    val chart = getDistChart(valMap)

    SwingWrapper(chart).displayChart()

//    val valMap = mutableMapOf<Double, Int>()
//    numberList.forEach { valMap.merge(it, 1, Integer::sum) }
//
//    val xList = mutableListOf<Double>(-1.0)
//    val yList = mutableListOf<Double>(0.0)
//
//    valMap.forEach { d, i ->
//        xList += d
//        yList += i.toDouble()
//    }
//    xList += 1.1
//    yList += 0.0

//    val xData = xList.toDoubleArray()
//    val yData = yList.toDoubleArray()
//
//    val chart  = QuickChart.getChart("Sample", "X", "Y", "y(x)", xData, yData)
//    SwingWrapper(chart).displayChart()

}

fun getDistChart(valMap: Map<Int, Int>): CategoryChart {
    val chart = CategoryChartBuilder().title("Histogram").build()
    chart.styler.setHasAnnotations(true)

    chart.addSeries("Distr", valMap.keys.toIntArray(), valMap.values.toIntArray())
    return chart
}
