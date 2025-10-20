package infrastructure.persistence.mock

import application.port.out.ResourceRepository
import database.Resource

class MockResourceRepositoryAdapter : ResourceRepository {
        override fun findByPath(path: String): Resource? {
            return MockDatabase.resources.find { it.path == path }
        }

        override fun findAll(): List<Resource> {
            return MockDatabase.resources
        }
}