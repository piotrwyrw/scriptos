package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.GroupsApi
import dev.piotrwyrw.scriptos.api.model.*
import dev.piotrwyrw.scriptos.service.GroupService
import dev.piotrwyrw.scriptos.util.currentUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupsController(
    val groupService: GroupService
) : GroupsApi {

    override fun createGroup(createGroupRequest: CreateGroupRequest): ResponseEntity<GroupIdResponse> {
        return ResponseEntity.ok(GroupIdResponse(groupService.createGroup(createGroupRequest.name, currentUser()!!)))
    }

    override fun addUserToGroup(addUserToGroupRequest: AddUserToGroupRequest): ResponseEntity<Unit> {
        groupService.addUserToGroup(addUserToGroupRequest)
        return ResponseEntity.ok().build()
    }

    override fun deleteUserFromGroup(removeUserFromGroupRequest: RemoveUserFromGroupRequest): ResponseEntity<Unit> {
        groupService.removeUserFromGroup(removeUserFromGroupRequest)
        return ResponseEntity.ok().build()
    }

    override fun leaveGroup(leaveGroupRequest: LeaveGroupRequest): ResponseEntity<Unit> {
        groupService.leaveGroup(leaveGroupRequest.groupName)
        return ResponseEntity.ok().build()
    }

}