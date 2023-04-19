package com.danilau.array

val nums = intArrayOf(3, 11, 6, 15)
val target = 9

fun main() {
    val res = twoSumsOptimized(nums, target)

    println("Result: ${res.joinToString()}")
}


// Brute force
fun twoSum(nums: IntArray, target: Int): IntArray {
    for (i in nums.indices) {
        for (j in i + 1 until nums.size) {
            if (nums[i] + nums[j] == target) {
                return intArrayOf(i, j)
            }
        }
    }
    return intArrayOf()
}

fun twoSumsOptimized(nums: IntArray, target: Int): IntArray {
    val numsMap = mutableMapOf<Int, Int>()
    nums.forEachIndexed { index, i ->
        val requiredNum = target - nums[index]
        if (numsMap.containsKey(requiredNum)) {
            return intArrayOf(numsMap[requiredNum]!!, index)
        }
        numsMap[nums[index]] = index
    }
    return intArrayOf()
}
