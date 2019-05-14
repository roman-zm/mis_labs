package lab7

import com.github.penemue.keap.PriorityQueue
import distribution.UniformDistribution
import java.util.*

class Lab7 {
    val queue = PriorityQueue<ICommand>(
        Comparator { l, r -> l.time.compareTo(r.time) }
    )

    var time = 0

    val X1 = 1
    val X2 = 2

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

    val firstGenerator = RequestGenerator(X1, firstStream)
    val secondGenerator = RequestGenerator(X2, secondStream)

    val generatorList = listOf(firstGenerator, secondGenerator)

    val history: MutableList<Request> = mutableListOf()

    fun start(timeBound: Int) {
        firstGenerator.distribution = UniformDistribution(
            0.0, 10.0, 1
        )
        scheduleFirstRequest()
        secondGenerator.distribution = UniformDistribution(
            0.0, 13.0, 2
        )
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
            println(time)
        }
        history.forEach {
            println("${it.type}:${it.number}: u = ${it.endTime - it.beginTime}")
        }
    }

    private fun processCommand(command: ICommand) {
        when (command) {
            is NewRequestCommand -> newRequest(command)
            is RouteRequestCommand -> routeRequest(command)
            is CompositeCommand -> command.commandList.forEach {
                processCommand(it)
            }
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

    fun addCommand(command: ICommand) {
        queue += command
    }

}

fun main() {
    val lab = Lab7()
    lab.start(100)
}