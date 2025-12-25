package lab7.infrastructure.persistence.jpa.adapter

import application.port.out.ResourceRepository
import database.Resource
import lab7.infrastructure.persistence.jpa.repository.JpaResourceRepository
import org.springframework.stereotype.Repository

@Repository
class JpaResourceRepositoryAdapter(
    private val jpaResourceRepository: JpaResourceRepository
) : ResourceRepository {

    override fun findByPath(path: String): Resource? =
        jpaResourceRepository.findByPath(path)?.toDomain()

    override fun findAll(): List<Resource> =
        jpaResourceRepository.findAll().map { it.toDomain() }
}