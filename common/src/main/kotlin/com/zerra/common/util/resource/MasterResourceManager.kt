package com.zerra.common.util.resource

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object MasterResourceManager {

    private val resourceManagers = hashMapOf<String, ResourceManager>()

    fun createResourceManager(root: String, domain: String): ResourceManager {
        val lowerDomain = domain.toLowerCase()

        if (resourceManagers[lowerDomain] != null) {
            throw IllegalStateException("Attempted to register a domain that already exists: $domain")
        }

        val manager = ResourceManager(root, domain)

        resourceManagers[domain] = manager

        return manager
    }

    fun getResourceManager(domain: String) = resourceManagers[domain.toLowerCase()]
}