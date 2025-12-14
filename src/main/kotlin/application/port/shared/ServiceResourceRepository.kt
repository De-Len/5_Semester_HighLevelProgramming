package application.port.shared

import application.port.out.ResourceRepository
import database.Resource
import org.springframework.stereotype.Service


@Service
class ServiceResourceRepository(private val resourceRepository: ResourceRepository) {
    fun findByPath(path: String): Resource? =
        resourceRepository.findByPath(path)
}