package lab7.infrastructure.persistence.jpa.repository

import lab7.infrastructure.persistence.jpa.entity.ResourceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaResourceRepository : JpaRepository<ResourceEntity, Long> {
    fun findByPath(path: String): ResourceEntity?
    fun existsByPath(path: String): Boolean
}