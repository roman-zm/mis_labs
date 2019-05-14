package lab7

import distribution.Distribution
import java.util.*
import kotlin.math.roundToInt

class Unit(
    val name: String,
    val owner: Lab7,
    var state: State = State.WAIT
) {
    val id: UUID = UUID.randomUUID()

    private var timeInWork: Int = 0

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
        owner.addCommand(command)
    }

    private fun process(request: Request) {

        if (request.beginTime == -1) {
            request.beginTime = owner.time
        }

        timeInWork = request.stream.getUnitTime(this) + owner.time

        request.path += this

        state = State.IN_WORK
        owner.addCommand(CompositeCommand(
            listOf(
                ChangeStateCommand(id, State.WAIT, timeInWork),
                RouteRequestCommand(request, timeInWork)
            ), timeInWork
        ))
    }

    fun changeState(state: Unit.State) {
        timeInWork = 0
        this.state = state
    }


    enum class State {
        WAIT, IN_WORK
    }
}

data class ChangeStateCommand(
    val id: UUID, val state: Unit.State, override val time: Int
) : ICommand

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
    val owner: Lab7
) {
    val id: UUID = UUID.randomUUID()

    fun onNext(request: Request) {
        val unit = path.getOrNull(request.path.size)
        if (unit != null) {
            unit.schedule(request)
        } else {
            request.endTime = owner.time
            owner.history += request
        }
    }

    fun getUnitTime(unit: Unit): Int {
        return distributions[path.indexOf(unit)]
            .getNextNumber().roundToInt() + 1
    }
}

interface ICommand {
    val time: Int
}

class NewRequestCommand(
    val type: Int,
    override val time: Int
): ICommand

class CompositeCommand(
    val commandList: List<ICommand>,
    override val time: Int
): ICommand

data class RouteRequestCommand(
    val request: Request,
    override val time: Int
): ICommand


class Request(
    val number: Int,
    val type: Int,
    val stream: RequestStream,
    val path: MutableList<Unit> = mutableListOf()
) {
    var beginTime = -1
    var endTime = -1
}