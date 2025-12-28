package lab7.infrastructure.persistence.jpa.entity

import database.Permission
import domain.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "permissions")
data class PermissionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userLogin: String,

    @Column(nullable = false)
    val resourcePath: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)