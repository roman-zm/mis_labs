package distribution

import java.lang.Math.abs

class TriangleDistribution(
    a: Double = 0.0,
    b: Double = 10.0
): SimpsonDistribution by TrapezialDistribution(
    abs(b - a) / 4, abs(b - a) / 4, 0.0
)
