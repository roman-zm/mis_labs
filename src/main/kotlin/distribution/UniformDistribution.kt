package distribution

import kotlin.random.Random

class UniformDistribution(
    val from: Double, val to: Double
): Distribution {
    val rand = Random(0)

    override fun getNextNumber() = rand.nextDouble(from, to)

    override val length
        get() = to - from
}