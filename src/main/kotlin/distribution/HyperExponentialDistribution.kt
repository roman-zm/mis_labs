package distribution

class HyperExponentialDistribution(
    override val lambda1: Double = 0.5,
    override val lambda2: Double = 1.5,
    override val q: Double = 0.4
) : IHyperExponentialDistribution {
    init { assert(q > 0 && q < 1) }

    private val firstExponentialDistribution = ExponentialDistribution(lambda1)
    private val secondExponentialDistribution = ExponentialDistribution(lambda2,
        SimpleCongruencesUniformDistribution(0.0, 1.0, 654)
    )

    private val uniformDistribution = LinearCongruenceUniformDistribution()

    override val length = 1.0

    override fun getNextNumber() = uniformDistribution.getNextNumber()
        .let{
            when {
                it > q -> firstExponentialDistribution
                else -> secondExponentialDistribution
            }.getNextNumber()
        }
}