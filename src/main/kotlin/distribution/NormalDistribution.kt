package distribution

import java.util.*

class NormalDistribution (
    override val expectedValue: Double = 0.0,
    override val standardDeviation: Double = 1.0
): INormalDistribution {
    val rand = Random(0)

    override fun getNextNumber()=
        rand.nextGaussian() * standardDeviation + expectedValue

    override val length
        get() = 1.0
}