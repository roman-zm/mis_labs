package distribution

import kotlin.math.max
import kotlin.math.min

class TrapezialDistribution(
    private val l1: Double,
    private val l2: Double,
    private val a: Double
): SimpsonDistribution {

    val firstStart = 0.0
    val firstEnd = l1 * 2

    val secondStart = 0.0
    val secondEnd = l2 * 2

    private val firstUniformDistribution: Distribution = SimpleCongruencesUniformDistribution(firstStart, firstEnd)
    private val secondUniformDistribution: Distribution = SimpleCongruencesUniformDistribution(secondStart, secondEnd, 1520)

    override fun getNextNumber() =
        a + firstUniformDistribution.getNextNumber() + secondUniformDistribution.getNextNumber()

    override val start = max(firstStart, secondStart) + a

    override val length: Double = firstEnd + secondEnd - max(firstStart, secondStart)

}