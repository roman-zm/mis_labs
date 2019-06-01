package distribution

class ExponentialDistribution(
    override val lambda: Double,
    private val uniformDistribution: Distribution = UniformDistribution()
) : IExponentialDistribution {

    override fun getNextNumber()
            = -1 / lambda * Math.log(uniformDistribution.getNextNumber())

    val expectedValue = 1 / lambda
    val standartDeviation = 1 / Math.pow(lambda, 2.0)
}