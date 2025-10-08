package database

import services.HashingService
import enums.Role

object MockDatabase {
    val users: List<User>
    val resources: List<Resource>
    val permissions: List<Permission>

    init {
        val user1Salt = HashingService.generateSalt()
        val user2Salt = HashingService.generateSalt()

        users = listOf(
            User("user1", HashingService.hashPassword("pass1", user1Salt), HashingService.saltToBase64(user1Salt)),
            User("user2", HashingService.hashPassword("pass2", user2Salt), HashingService.saltToBase64(user2Salt))
        )

        resources = listOf(
            Resource("A", 100),
            Resource("A.B", 50),
            Resource("A.B.C", 20),
            Resource("D.E", 10)
        )

        permissions = listOf(
            Permission("user1", "A", Role.READ),
            Permission("user1", "A.B", Role.WRITE),
            Permission("user2", "A.B.C", Role.EXECUTE)
        )
    }
}