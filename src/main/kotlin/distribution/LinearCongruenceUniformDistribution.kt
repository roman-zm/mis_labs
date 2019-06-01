package distribution

class LinearCongruenceUniformDistribution(
    override val from: Double = 0.0,
    override val to: Double = 1.0,
    seed: Int = 3229,
    val a: Int = 45,
    val c: Int = 47,
    val m: Int = (2 pow 12).toInt()
): IUniformDistribution {
    override fun getNextNumber() =
        ((a * prevElement + c) % m).apply {
            prevElement = this
        }.toDouble() / m * length + from

    override val length = to - from

    private var prevElement = seed
}

