package distribution

class ErlangDistribution(
    override val a: Int,
    override val b: Double
) : IErlangDistribution {
    override fun getNextNumber()
            = exponentialDistributions.map { it.getNextNumber() }.sum()

    private val lambda = 1 / b

    private val exponentialDistributions = Array(a) {
        ExponentialDistribution(lambda, UniformDistribution(seed = it * 123))
    }

}