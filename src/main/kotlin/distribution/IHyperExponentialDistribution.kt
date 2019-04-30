package distribution

interface IHyperExponentialDistribution: Distribution {
    val lambda1: Double
    val lambda2: Double
    val q: Double
}