package com.mrboomdev.uust.data

enum class University(
    val address: String,
    val campuses: List<Campus>
) {
    UGATU(
        address = "Карла Маркса 12",
        campuses = listOf(
            Campus( // 1
                floors = listOf(
                    Floor(roomsCount = 9), // 1
                    Floor(roomsCount = 9), // 2
                    Floor(roomsCount = 9), // 3
                    Floor(roomsCount = 9), // 4
                    Floor(roomsCount = 9)  // 5
                )
            )
        )
    ),

    BGU(
        address = "Заки Валиди 32",
        campuses = listOf()
    ),

    DORMITORY2(
        address = "Мингажева 160",
        campuses = listOf()
    )
}

data class Campus(
    val floors: List<Floor>
)

data class Floor(
    val roomsCount: Int
)