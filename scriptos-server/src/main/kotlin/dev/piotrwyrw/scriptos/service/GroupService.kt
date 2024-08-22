package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.api.model.AddUserToGroupRequest
import dev.piotrwyrw.scriptos.api.model.RemoveUserFromGroupRequest
import dev.piotrwyrw.scriptos.config.GroupsConfig
import dev.piotrwyrw.scriptos.exception.ScriptosException
import dev.piotrwyrw.scriptos.persistence.model.GroupEntity
import dev.piotrwyrw.scriptos.persistence.model.UserEntity
import dev.piotrwyrw.scriptos.persistence.repository.GroupRepository
import dev.piotrwyrw.scriptos.persistence.repository.UserRepository
import dev.piotrwyrw.scriptos.util.currentUser
import dev.piotrwyrw.scriptos.util.isValidGroupName
import jakarta.transaction.Transactional
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

    fun createGroup(name: String, admin: UserEntity): UUID {
        if (!name.isValidGroupName())
            throw ScriptosException(
                "The group name does not match the required pattern",
                HttpStatus.UNPROCESSABLE_ENTITY
            )

        if (groupNameTaken(name))
            throw ScriptosException("This group name is already taken", HttpStatus.CONFLICT)

        val group = GroupEntity()
        group.name = name
        group.adminUser = admin.id
        groupRepository.save(group)

        logger.info("Created group \"${name}\" with admin user \"${admin.username}\"")

        return group.id
    }

    @Transactional
    fun addUserToGroup(username: String, groupName: String, checkPermissions: Boolean) {
        val group = groupByName(groupName) ?: throw ScriptosException(
            "The group '${groupName}' does not exist",
            HttpStatus.NOT_FOUND
        )

        val user = userService.byUsername(username) ?: throw ScriptosException(
            "The user '${username}' does not exist",
            HttpStatus.NOT_FOUND
        )

        if (checkPermissions && (group.adminUser != currentUser()?.id && currentUser()?.id != userService.systemAdministrator().id))
            throw ScriptosException("You do not have permissions to do this.", HttpStatus.FORBIDDEN)

        if (group.members.contains(user))
            throw ScriptosException("The user '${username}' is already in group '${groupName}'", HttpStatus.CONFLICT)

        user.groups.add(group)
        group.members.add(user)

        userRepository.save(user)
        groupRepository.save(group)

        logger.info("User \"${username}\" added to group \"${groupName}\"")
    }

    // TODO Make the code less duplicated
    fun removeUserFromGroup(username: String, groupName: String, checkPermissions: Boolean) {
        val group = groupByName(groupName) ?: throw ScriptosException(
            "The group '${groupName}' does not exist",
            HttpStatus.NOT_FOUND
        )

        val user = userService.byUsername(username) ?: throw ScriptosException(
            "The user '${username}' does not exist",
            HttpStatus.NOT_FOUND
        )

        if (checkPermissions && (group.adminUser != currentUser()?.id && currentUser()?.id != userService.systemAdministrator().id && currentUser()?.id != user.id))
            throw ScriptosException("You do not have permissions to do this.", HttpStatus.FORBIDDEN)

        if (!group.members.contains(user))
            throw ScriptosException("The user '${username}' is not in group '${groupName}'", HttpStatus.BAD_REQUEST)

        if (groupName == commonGroupName())
            throw ScriptosException("A user cannot be removed from the common group.", HttpStatus.BAD_REQUEST)

        if (user.id == group.adminUser)
            throw ScriptosException("The admin of a group cannot be removed from said group", HttpStatus.BAD_REQUEST)

        user.groups.remove(group)
        group.members.remove(user)

        userRepository.save(user)
        groupRepository.save(group)

        logger.info("User \"${username}\" removed from group \"${groupName}\"")
    }

    fun leaveGroup(groupName: String) =
        removeUserFromGroup(currentUser()!!.username, groupName, checkPermissions = true)

    @Transactional
    fun addUserToGroup(username: String, groupName: String) =
        addUserToGroup(username, groupName, checkPermissions = true)

    @Transactional
    fun addUserToCommonGroup(username: String) = addUserToGroup(username, commonGroupName(), checkPermissions = false)

    @Transactional
    fun addUserToGroup(request: AddUserToGroupRequest) = addUserToGroup(request.username, request.groupName)

    fun removeUserFromGroup(request: RemoveUserFromGroupRequest) =
        removeUserFromGroup(request.username, request.groupName, checkPermissions = true)

    fun groupByName(name: String) = groupRepository.findByName(name).getOrNull()

    fun groupNameTaken(name: String) = groupByName(name) != null

    fun commonGroupName() = groupsConfig.commonGroupName

}