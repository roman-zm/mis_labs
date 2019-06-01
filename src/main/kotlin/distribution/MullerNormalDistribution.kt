package distribution

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sqrt

class MullerNormalDistribution(
    override val expectedValue: Double = 0.0,
    override val standardDeviation: Double = 1.0
) : INormalDistribution {

    override fun getNextNumber()=
        sqrt(-2 * ln(firstUniformDistribution.getNextNumber())) *
                cos(2 * PI * secondUniformDistribution.getNextNumber())

    private val firstUniformDistribution =
        SimpleCongruencesUniformDistribution(0.0, 1.0)

    private val secondUniformDistribution =
        SimpleCongruencesUniformDistribution(0.0, 1.0, 4563)
}