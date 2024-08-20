package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.GroupsConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.GroupEntity
import dev.piotrwyrw.scriptos.persistence.repository.GroupRepository
import dev.piotrwyrw.scriptos.util.isValidGroupName
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val groupsConfig: GroupsConfig
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun ensureCommonGroupExists() {
        val commonName = groupsConfig.commonGroupName
        groupByName(commonName) ?: run {
            createGroup(commonName)
            logger.info("Created the common group \"${commonName}\"")
        }
    }

    fun createGroup(name: String): UUID {
        if (!name.isValidGroupName())
            throw ScriptosException(
                "The group name does not match the required pattern",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        val group = GroupEntity()
        group.name = name
        groupRepository.save(group)
        return group.id
    }

    fun groupByName(name: String) = groupRepository.findByName(name).getOrNull()

}