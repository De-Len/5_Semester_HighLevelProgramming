package lab7.application.port.shared

import application.port.out.ResourceRepository
import database.Resource
import org.springframework.stereotype.Service


@Service
open class ServiceResourceRepository(private val resourceRepository: ResourceRepository) {
    fun findByPath(path: String): Resource? =
        resourceRepository.findByPath(path)
}