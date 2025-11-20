package database

import domain.enums.Role

data class Permission(val userLogin: String, val resourcePath: String, val role: Role)

data class User(val login: String, val passHash: String, val salt: String)

data class Resource(val path: String, val maxVolume: Int)