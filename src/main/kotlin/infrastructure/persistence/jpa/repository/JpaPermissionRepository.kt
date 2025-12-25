package lab7.infrastructure.persistence.jpa.repository

import lab7.infrastructure.persistence.jpa.entity.PermissionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPermissionRepository : JpaRepository<PermissionEntity, Long> {
    fun findAllByUser_Id(userId: Long): List<PermissionEntity>
}