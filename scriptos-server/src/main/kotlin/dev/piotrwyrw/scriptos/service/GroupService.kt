package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.AddUserToGroupRequest
import dev.piotrwyrw.scriptos.config.GroupsConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.GroupEntity
import dev.piotrwyrw.scriptos.persistence.repository.GroupRepository
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import dev.piotrwyrw.scriptos.util.isValidGroupName
import jakarta.annotation.PostConstruct
import org.hibernate.tool.schema.spi.ScriptTargetOutput
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val groupsConfig: GroupsConfig,
    val userService: UserService,
    private val userRepository: UserRepository
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

        if (groupNameTaken(name))
            throw ScriptosException("This group name is already taken", HttpStatus.CONFLICT)

        val group = GroupEntity()
        group.name = name
        groupRepository.save(group)
        return group.id
    }

    fun addUserToGroup(username: String, groupName: String) {
        val group = groupByName(groupName) ?: throw ScriptosException(
            "The group '${groupName}' does not exist",
            HttpStatus.NOT_FOUND
        )

        val user = userService.byUsername(username) ?: throw ScriptosException(
            "The user '${username}' does not exist",
            HttpStatus.NOT_FOUND
        )

        if (group.members.contains(user))
            throw ScriptosException("The user '${username}' is already in group '${groupName}'", HttpStatus.CONFLICT)

        user.groups.add(group)
        group.members.add(user)
        userRepository.save(user)
        groupRepository.save(group)

        logger.info("User \"${username}\" added to group \"${groupName}\"")
    }

    fun addUserToGroup(request: AddUserToGroupRequest) = addUserToGroup(request.username, request.groupName)

    fun groupByName(name: String) = groupRepository.findByName(name).getOrNull()

    fun groupNameTaken(name: String) = groupByName(name) != null

}