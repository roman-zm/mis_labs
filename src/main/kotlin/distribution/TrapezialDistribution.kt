package distribution

import kotlin.math.min

class TrapezialDistribution(
    l1: Double,
    l2: Double,
    private val a: Double
): ISimpsonDistribution {

    override fun getNextNumber()
            = a + firstUniformDistribution.getNextNumber() + secondUniformDistribution.getNextNumber()

    override val start by lazy {
        a + min(firstUniformDistribution.from, secondUniformDistribution.from)
    }

    override val length by lazy {
        firstUniformDistribution.to + secondUniformDistribution.to - start
    }

    private val firstUniformDistribution: IUniformDistribution
            = SimpleCongruencesUniformDistribution(.0, l1 * 2)

    private val secondUniformDistribution: IUniformDistribution
            = SimpleCongruencesUniformDistribution(.0, l2 * 2, 1520)

}