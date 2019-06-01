package distribution

class HyperExponentialDistribution(
    override val lambda1: Double = 1.5,
    override val lambda2: Double = 4.5,
    override val q: Double = 0.4
) : IHyperExponentialDistribution {
    init { assert(q > 0 && q < 1) }

    override fun getNextNumber() = exponentialDistribution.getNextNumber()

    private val uniformDistribution = UniformDistribution()
    private val firstExponentialDistribution = ExponentialDistribution(lambda1)
    private val secondExponentialDistribution = ExponentialDistribution(lambda2,
        UniformDistribution(0.0, 1.0, 654)
    )

    private val exponentialDistribution: ExponentialDistribution
        get() = uniformDistribution.getNextNumber().let {
            when {
                it > q -> firstExponentialDistribution
                else -> secondExponentialDistribution
            }
        }

}
