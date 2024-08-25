package dev.piotrwyrw.scriptos.controller

import dev.piotrwyrw.scriptos.api.StatisticsApi
import dev.piotrwyrw.scriptos.api.model.DiskUsageResponse
import dev.piotrwyrw.scriptos.service.StatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticsController (
    val statisticsService: StatisticsService
): StatisticsApi {

    override fun getDiskUsage(): ResponseEntity<DiskUsageResponse> {
        return ResponseEntity.ok(DiskUsageResponse(statisticsService.diskUsage()))
    }

}