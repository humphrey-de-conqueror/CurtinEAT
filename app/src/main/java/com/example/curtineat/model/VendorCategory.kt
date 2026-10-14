package com.example.curtineat.model

enum class VendorCategory(
    val displayName: String
) {
    WESTERN("Western"),
    JAPANESE("Japanese"),
    CHINESE("Chinese"),
    KOREAN("Korean"),
    LOCAL("Local"),
    FAST_FOOD("Fast Food"),
    DESSERT("Dessert"),
    BEVERAGE("Beverage"),
    OTHER("Other");

    companion object {
        fun fromDisplayName(
            displayName: String
        ): VendorCategory? {
            return entries.firstOrNull {
                it.displayName.equals(
                    displayName,
                    ignoreCase = true
                )
            }
        }
    }
}