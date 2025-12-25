package lab7.infrastructure.persistence.jpa.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "resources",
    uniqueConstraints = [UniqueConstraint(columnNames = ["path"])]
)
class ResourceEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 255)
    val path: String,

    @Column(nullable = false)
    val maxVolume: Int
)