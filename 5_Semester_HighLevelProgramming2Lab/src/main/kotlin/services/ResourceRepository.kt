package services

import database.Resource
import application.port.out.ResourceRepository as IResourceRepository // Избегаем конфликта имен


class ResourceRepository(private val resourceRepository: IResourceRepository) {
    fun findByPath(path: String): Resource? =
        resourceRepository.findByPath(path)
}