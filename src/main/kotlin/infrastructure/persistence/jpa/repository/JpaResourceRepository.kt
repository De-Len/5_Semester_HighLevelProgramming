package lab7.infrastructure.persistence.jpa.repository

import application.port.out.ResourceRepository
import database.Resource
import database.User
import lab7.infrastructure.persistence.jpa.entity.ResourceEntity
import lab7.infrastructure.persistence.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ResourceJpaRepository : JpaRepository<ResourceEntity, String>

@Repository
class JpaResourceRepositoryAdapter(
    private val repository: ResourceJpaRepository
) : ResourceRepository {
    override fun findByPath(path: String) = repository.findById(path).orElse(null).toDomain()
    override fun findAll() = repository.findAll().map { it.toDomain() }

    // Расширение для конвертации Entity → Domain
    private fun ResourceEntity.toDomain(): Resource {
        return Resource(
            path = this.path,
            maxVolume = this.maxVolume
        )
    }
}