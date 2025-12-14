package lab7.infrastructure.persistence.mock

import application.port.out.ResourceRepository
import database.Resource
import infrastructure.persistence.mock.MockDatabase
import org.springframework.stereotype.Repository

@Repository
open class MockResourceRepositoryAdapter : ResourceRepository {
        override fun findByPath(path: String): Resource? {
            return MockDatabase.resources.find { it.path == path }
        }

        override fun findAll(): List<Resource> {
            return MockDatabase.resources
        }
}