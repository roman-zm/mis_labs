package distribution

import kotlin.random.Random

class UniformDistribution(
    val from: Double, val to: Double, seed: Int = 0
): Distribution {
    val rand = Random(seed)

    override fun getNextNumber() = rand.nextDouble(from, to)

    override val length
        get() = to - from
}