package com.entertaining.maths.util

import java.security.SecureRandom

object ProblemGenerator {

    fun generateProblem(lvl: Int): Problem {
        println(lvl)
        assert(lvl in 1..3)

        var problem: String
        var a: Int
        var b: Int
        var c: Int
        var n1 = 0
        var n2 = 0

        do {
            val random = SecureRandom.getInstanceStrong()
            when (lvl) {
                1 -> {
                    n1 = random.nextLong(1, 2 + 1).toInt()
                    n2 = random.nextLong(1, 2 + 1).toInt()
                }
                2 -> {
                    n1 = random.nextLong(1, 3 + 1).toInt()
                    n2 = random.nextLong(1, 3 + 1).toInt()
                }
                3 -> {
                    n1 = random.nextLong(1, 4 + 1).toInt()
                    n2 = random.nextLong(1, 4 + 1).toInt()
                }
            }

            a = random.nextLong(1, lvl * 33L + 1).toInt()
            b = random.nextLong(1, lvl * 33L + 1).toInt()
            c = random.nextLong(1, lvl * 33L + 1).toInt()

            problem = when (n2) {
                3, 4 -> "("
                else -> ""
            }
            problem += a.toString()
            when (n1) {
                1 -> problem += " + "
                2 -> problem += " - "
                3 -> problem += " * "
                4 -> problem += " / "
            }
            problem += b.toString()
            problem += when (n2) {
                3, 4 -> ")"
                else -> ""
            }
            when (n2) {
                1 -> problem += " + "
                2 -> problem += " - "
                3 -> problem += " * "
                4 -> problem += " / "
            }
            problem += c.toString()
            problem += " ="

        } while (!isOk(a, b, c, n1, n2).first)

        return Problem(problem, isOk(a, b, c, n1, n2).second!!)
    }

    private fun isOk(a: Int, b: Int, c: Int, n1: Int, n2: Int): Pair<Boolean, Int?> {
        var sum = a
        when (n1) {
            1 -> sum += b
            2 -> sum -= b
            3 -> sum *= b
            4 -> {
                if (((sum / b.toDouble()).toInt().toDouble()) != (sum / b.toDouble())) return false to null
                sum /= b
            }
        }

        when (n2) {
            1 -> sum += c
            2 -> sum -= c
            3 -> sum *= c
            4 -> {
                if (((sum / c.toDouble()).toInt().toDouble()) != (sum / c.toDouble())) return false to null
                sum /= c
            }
        }

        return if (sum in 0..100) true to sum else false to null
    }

    private fun java.util.Random.nextLong(min: Long, max: Long): Long =
        longs(1, min, max).reduce(0) { _, b -> b }

    data class Problem(
        val problem: String,
        val result: Int,
    )
}
