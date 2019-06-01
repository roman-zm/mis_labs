package lab7

import distribution.Distribution
import java.util.*
import kotlin.math.roundToInt

class Unit(
    val name: String,
    val scheduler: Scheduler,
    var state: State = State.WAIT
) {
    val id: UUID = UUID.randomUUID()

    private var timeInWork: Int = 0
    private var workDuration: Int = 0

    private var mTotalTimeInWork: Int = 0
    val totalTimeInWork: Int
        get() = mTotalTimeInWork

    fun schedule(request: Request) {
        when (state) {
            State.WAIT -> process(request)
            State.IN_WORK -> postRequest(request)
        }
    }

    private fun postRequest(request: Request) {
        val command = RouteRequestCommand(
            request, timeInWork
        )
        scheduler.addCommand(command)
    }

    private fun process(request: Request) {

        if (request.beginTime == -1) {
            request.beginTime = scheduler.time
        }

        workDuration = request.stream.getUnitTime(this)
        timeInWork = workDuration + scheduler.time

        request.path += this

        state = State.IN_WORK
        scheduler.addCommand(
            ChangeStateCommand(id, State.WAIT, timeInWork),
            RouteRequestCommand(request, timeInWork)
        )
    }

    fun changeState(state: Unit.State) {
        mTotalTimeInWork += workDuration
        workDuration = 0
        timeInWork = 0
        this.state = state

        timeInWorkHistory += scheduler.time to mTotalTimeInWork
    }

    /**
     * key - current time
     * value - timeInWork
     */
    val timeInWorkHistory = mutableMapOf<Int, Int>()

    enum class State {
        WAIT, IN_WORK
    }
}

class RequestGenerator(
    val type: Int,
    val stream: RequestStream
) {

    fun generate() {
        stream.onNext(Request(number, type, stream))
    }
    fun schedule(): ICommand {
        return NewRequestCommand(type, nextTime)
    }

    lateinit var distribution: Distribution

    private val nextTime: Int
        get() {
            val time = lastTime + distribution.getNextNumber().roundToInt()
            lastTime = time
            return time
        }

    private var lastTime = 0

    private var _number = 0

    private val number: Int
        get() = _number++
}
class RequestStream(
    val type: Int,
    val path: List<Unit>,
    val distributions: List<Distribution>,
    val scheduler: Scheduler
) {

    val id: UUID = UUID.randomUUID()
    fun onNext(request: Request) {
        val unit = path.getOrNull(request.path.size)
        if (unit != null) {
            unit.schedule(request)
        } else {
            request.endTime = scheduler.time
            scheduler.history += request
        }
    }

    fun getUnitTime(unit: Unit): Int {
        return distributions[path.indexOf(unit)]
            .getNextNumber().roundToInt() + 1
    }

}
interface Timed {
    val time: Int
}

interface Typed {
    val type: Int
}

interface ICommand: Timed, Typed

class NewRequestCommand(
    override val type: Int,
    override val time: Int
): ICommand

data class RouteRequestCommand(
    val request: Request,
    override val time: Int
): ICommand, Typed by request

/**
 * Приоритет смены состояния всегда наивысший, те минимальное значения Int для мин кучи
 */
data class ChangeStateCommand(
    val id: UUID, val state: Unit.State, override val time: Int
) : ICommand {
    override val type: Int = Integer.MIN_VALUE
}

class Request(
    val number: Int,
    override val type: Int,
    val stream: RequestStream,
    val path: MutableList<Unit> = mutableListOf()
): Typed {
    var beginTime = -1
    var endTime = -1
}

