package distribution

import java.util.*

class NormalDistribution (
    override val expectedValue: Double = 0.0,
    override val standardDeviation: Double = 1.0,
    seed: Long = 0
): INormalDistribution {
    override fun getNextNumber()=
        rand.nextGaussian() * standardDeviation + expectedValue

    private val rand = Random(seed)
}