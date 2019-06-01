package distribution

import kotlin.random.Random

class UniformDistribution(
    override val from: Double = 0.0,
    override val to: Double = 1.0,
    seed: Int = 0
): IUniformDistribution {
    override fun getNextNumber() = rand.nextDouble(from, to)

    override val length = to - from

    private val rand = Random(seed)
}