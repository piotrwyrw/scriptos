import {Injectable, signal} from '@angular/core';
import {GroupListingResponseInner, GroupsService} from "../openapi";
import {NotificationService} from "./notification.service";

@Injectable({
  providedIn: 'root'
})
export class BackendGroupService {

  groups: GroupListingResponseInner[] = []

  loadingGroups = signal(false)

  constructor(private groupService: GroupsService, private notificationService: NotificationService) {
    this.loadGroups()
  }

  loadGroups() {
    this.loadingGroups.set(true)
    this.groupService.retrieveGroups().subscribe({
      next: resp => {
        this.groups = resp
        this.loadingGroups.set(false)
      },
      error: err => {
        this.notificationService.displayResponse(err.error)
        this.groups = []
        this.loadingGroups.set(false)
      }
    })
  }

}
