package dev.piotrwyrw.scriptos.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scriptos.groups")
class GroupsConfig (
    var commonGroupName: String = "common",
    var commonGroupDescription: String = "The default group"
)