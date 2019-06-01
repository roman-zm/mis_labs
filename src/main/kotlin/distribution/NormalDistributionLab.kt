package distribution;

class NormalDistributionLab (
        override val expectedValue: Double = 0.0,
        override val standardDeviation: Double = 1.0
): INormalDistribution {

    override fun getNextNumber() =
        (uniformDistributionList.map(nextNumber).sum() - uniformDistributionList.size / 2) *
                standardDeviation + expectedValue

    private val uniformDistributionList = Array(12) {
        UniformDistribution(0.0, 1.0, 234 + it * 30 + it * 1000)
    }

    private val nextNumber = IUniformDistribution::getNextNumber
}
