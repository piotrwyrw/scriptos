package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.AdminUserConfig
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AdminUserAndCommonGroupService(
    val groupService: GroupService,
    val userService: UserService,
    val adminUserConfig: AdminUserConfig
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    @PostConstruct
    fun ensureAdminUserAndCommonGroupExists() {
        val adminExists = ensureAdminUserExists()
        ensureCommonGroupExists()

        if (adminExists)
            return

        groupService.addUserToCommonGroup(adminUserConfig.username)
    }

    @Transactional
    fun ensureAdminUserExists(): Boolean {
        if (userService.byUsername(adminUserConfig.username) == null) {
            userService.createUser(adminUserConfig.username, adminUserConfig.password)
            logger.info("The admin user \"${adminUserConfig.username}\" was created with password \"${adminUserConfig.password}\"")
            return false
        }

        logger.info("Admin user: ${adminUserConfig.username}")
        logger.info("Admin password: ${adminUserConfig.password}")
        return true
    }

    @Transactional
    fun ensureCommonGroupExists() {
        val commonName = groupService.commonGroupName()
        groupService.groupByName(commonName) ?: run {
            groupService.createGroup(commonName, userService.systemAdministrator())
            logger.info("Created the common group \"${commonName}\"")
        }
    }

}