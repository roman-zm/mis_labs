package distribution

class ExponentialDistribution(
    override val lambda: Double,
    val uniformDistribution: Distribution = LinearCongruenceUniformDistribution()
) : IExponentialDistribution {

    override fun getNextNumber(): Double {
        return -1 / lambda * Math.log(uniformDistribution.getNextNumber());
    }

    override val length = 0.0

    val expectedValue = 1 / lambda
    val standartDeviation = 1 / Math.pow(lambda, 2.0)
}