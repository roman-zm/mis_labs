package distribution

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt

class MullerNormalDistribution(
    override val expectedValue: Double = 0.0,
    override val standardDeviation: Double = 1.0
) : INormalDistribution {

    private val firstUniformDistribution = SimpleCongruencesUniformDistribution(0.0, 1.0)
    private val secondUniformDistribution = SimpleCongruencesUniformDistribution(0.0, 1.0, 4563)

    override fun getNextNumber(): Double {
        return sqrt(-2 * ln(firstUniformDistribution.getNextNumber())) *
                cos(2 * PI * secondUniformDistribution.getNextNumber())
    }

    override val length = 1.0
}