package distribution;

class NormalDistributionLab (
        override val expectedValue: Double = 0.0,
        override val standardDeviation: Double = 1.0
): INormalDistribution {

    private val uniformDistributionList = Array(12) {
        SimpleCongruencesUniformDistribution(0.0, 1.0, 234 + it * 30 + it * 1000)
    }

    override fun getNextNumber() = (uniformDistributionList.map { it.getNextNumber() }.sum() -
            uniformDistributionList.size / 2) * standardDeviation + expectedValue

    override val length = 1.0

}
