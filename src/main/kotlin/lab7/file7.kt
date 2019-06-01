package lab7

import com.github.penemue.keap.PriorityQueue
import distribution.UniformDistribution
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.markers.SeriesMarkers
import java.util.*

class Scheduler {

    companion object {
        const val X1 = 2
        const val X2 = 1
    }

    val queue = PriorityQueue<ICommand>(
        Comparator { l, r ->
            l.time.compareTo(r.time).let {
                if (it == 0) l.type.compareTo(r.type)
                else it
            }
        }
    )

    var time = 0

    val processor = Unit("Процессор", this)
    val printer = Unit("Принтер", this)
    val monitor = Unit("Монитор", this)
    val keyboard = Unit("Клавиатура", this)

    val unitList = listOf(processor, printer, monitor, keyboard)

    val firstStream = RequestStream(
        X1, listOf(keyboard, processor, printer),
        listOf(
            UniformDistribution(0.0, 2.0, 3),
            UniformDistribution(0.0, 4.0, 4),
            UniformDistribution(0.0, 2.0, 5)
        ), this
    )
    val secondStream = RequestStream(
        X2, listOf(monitor, processor, monitor),
        listOf(
            UniformDistribution(0.0, 2.0, 6),
            UniformDistribution(0.0, 3.0, 7),
            UniformDistribution(0.0, 2.0, 8)
        ), this
    )
    val streamList = listOf(firstStream, secondStream)

    val firstGenerator = RequestGenerator(X1, firstStream).apply {
        distribution = UniformDistribution(0.0, 10.0, 1)
    }
    val secondGenerator = RequestGenerator(X2, secondStream).apply {
        distribution = UniformDistribution(0.0, 13.0, 2)
    }
    val generatorList = listOf(firstGenerator, secondGenerator)

    val history: MutableList<Request> = mutableListOf()

    fun start(timeBound: Int) {
        scheduleFirstRequest()
        scheduleSecondRequest()

        doWork(timeBound)
    }

    private fun doWork(timeBound: Int) {
        while (time < timeBound) {
            val command = queue.poll()
            command?.let {
                time = it.time
                processCommand(it)
            }
        }
        val printer = { request: Request ->
            with(request) {
                println("\tЗаявка $number: u = ${endTime - beginTime}")
            }
        }
        println("Реакции по потоку")
        println(" X1")
        history.filter { it.type == X1 }.forEach(printer)
        println(" X2")
        history.filter { it.type == X2 }.forEach(printer)

        println("Коэффициенты загрузки устройств")
        unitList.forEach {
            println("${it.name}: коэффициент загрузки = ${it.totalTimeInWork.toDouble() / timeBound}")
        }
    }

    private fun processCommand(command: ICommand) {
        when (command) {
            is NewRequestCommand -> newRequest(command)
            is RouteRequestCommand -> routeRequest(command)
            is ChangeStateCommand -> changeState(command)
        }
    }

    private fun changeState(command: ChangeStateCommand) {
        unitList.firstOrNull { it.id == command.id }
            ?.changeState(command.state)
    }

    private fun routeRequest(command: RouteRequestCommand) {
        streamList.firstOrNull {
            it.type == command.request.type
        }?.onNext(command.request)
    }

    private fun newRequest(command: NewRequestCommand) {
        val generator = generatorList.firstOrNull { it.type == command.type }
        generator?.generate()
        queue += generator?.schedule()
    }

    private fun scheduleFirstRequest() {
        queue += firstGenerator.schedule()
    }

    private fun scheduleSecondRequest() {
        queue += secondGenerator.schedule()
    }

    fun addCommand(vararg commands: ICommand) {
        commands.forEach { queue += it }
    }

}

fun main() {
    val lab = Scheduler()
    val timeBound = 2_000
    lab.start(timeBound)

    lab.unitList.forEach {
        printTimeInWork(it, timeBound)
    }
}

fun printTimeInWork(unit: Unit, timeBound: Int) {
    val history = unit.timeInWorkHistory

//    val map = mapWithSize(timeBound) { key ->
//        if (history.containsKey(key)) {
//            history[key]?.div(timeBound.toDouble()) ?: 0.0
//        } else {
//        val time = history.keys.lastOrNull { it <= key }
//            history[]?.div(timeBound.toDouble()) ?: 0.0
//        }
//    }
    val map = history.mapValues { (it.value.toDouble() / it.key) }

    val chart = getChart(map, unit.name)
    SwingWrapper(chart).displayChart()
}

fun getChart(valMap: Map<Int, Double>, name: String): XYChart {
    val chart = XYChartBuilder().title(name).build()
    chart.styler.setHasAnnotations(false)

    val series = chart.addSeries("Коэффициент загрузки", valMap.keys.map { it.toDouble() }.toDoubleArray(),
        valMap.values.map { it }.toDoubleArray())

    series.marker = SeriesMarkers.NONE
//    chart.styler.defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.StepArea
//    chart.styler.isXAxisLogarithmic = true

    return chart
}


fun <V> mapWithSize(size: Int, block: (Int) -> V): Map<Int, V> {
    val map = mutableMapOf<Int, V>()
    repeat(size) {
        map[it] = block(it)
    }
    return map.toMap()
}


