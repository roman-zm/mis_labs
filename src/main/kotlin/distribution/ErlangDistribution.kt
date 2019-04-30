package distribution

class ErlangDistribution(
    override val a: Int,
    override val b: Double
) : IErlangDistribution {

    val lambda = 1 / b

    val exponentialDistributions = Array(a) {
        ExponentialDistribution(lambda, LinearCongruenceUniformDistribution(seed = it * 123))
    }

    override fun getNextNumber()
            = exponentialDistributions.map { it.getNextNumber() }.sum()


    override val length = 1.0

}