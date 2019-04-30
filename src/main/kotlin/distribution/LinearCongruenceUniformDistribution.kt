package distribution

class LinearCongruenceUniformDistribution(
    private val from: Double = 0.0,
    private val to: Double = 1.0,
    seed: Int = 3229
): Distribution {

    private var prevElement = seed

    private val a = 45
    private val c = 47
    private val m = (2 pow 12).toInt()

    override fun getNextNumber() =
        ((a * prevElement + c) % m).apply {
            prevElement = this
        }.toDouble() / m * length + from

    override val length = to - from
}

