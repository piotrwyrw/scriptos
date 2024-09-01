package dev.piotrwyrw.scriptos.service

import dev.piotrwyrw.scriptos.config.AdminUserConfig
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Transactional
@Service
class AdminUserAndCommonGroupService(
    val groupService: GroupService,
    val userService: UserService,
    val adminUserConfig: AdminUserConfig
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun ensureAdminUserAndCommonGroupExists() {
        ensureAdminUserExists()
        ensureCommonGroupExists()
    }

    fun ensureAdminUserExists(): Boolean {
        if (userService.byUsername(adminUserConfig.username) == null) {
            userService.createUser(adminUserConfig.username, adminUserConfig.password)
            logger.info("The admin user \"${adminUserConfig.username}\" was created.")
            return false
        }

        logger.info("------------------------------")
        logger.info("Admin user: ${adminUserConfig.username}")
        logger.info("Admin password: ${adminUserConfig.password}")
        logger.info("------------------------------")
        return true
    }

    fun ensureCommonGroupExists() {
        val commonName = groupService.commonGroupName()
        val commonDescription = groupService.commonGroupDescription()
        groupService.groupByName(commonName) ?: run {
            groupService.createGroup(commonName, commonDescription, userService.systemAdministrator(), checkPermissions = false)
            logger.info("Created the common group \"${commonName}\"")
        }
    }

}