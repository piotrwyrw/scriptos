package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.GroupsApi
import dev.piotrwyrw.scriptos.api.model.CreateGroupRequest
import dev.piotrwyrw.scriptos.api.model.GroupIdResponse
import dev.piotrwyrw.scriptos.service.GroupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupsController(
    val groupService: GroupService
) : GroupsApi {

    override fun createGroup(createGroupRequest: CreateGroupRequest): ResponseEntity<GroupIdResponse> {
        return ResponseEntity.ok(GroupIdResponse(groupService.createGroup(createGroupRequest.name)))
    }

}