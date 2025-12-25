package lab7.infrastructure.persistence.jpa.entity

import domain.enums.Role
import jakarta.persistence.*


@Entity
@Table(
    name = "permissions",
    indexes = [
        Index(columnList = "user_id"),
        Index(columnList = "resource_id")
    ]
)
class PermissionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    val resource: ResourceEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)