package lab6

import distribution.Distribution

class Consumer(val distribution: Distribution) {
    val a: Int
        get() = distribution.getNextNumber().toInt()
}
