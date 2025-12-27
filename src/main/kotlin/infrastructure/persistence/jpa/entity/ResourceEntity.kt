package lab7.infrastructure.persistence.jpa.entity

import database.Resource
import jakarta.persistence.*

@Entity
@Table(name = "resources")
data class ResourceEntity(
    @Id
    val path: String,

    @Column(nullable = false)
    val maxVolume: Int
)