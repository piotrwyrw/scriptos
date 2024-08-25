import {EventEmitter, Injectable, signal} from '@angular/core';
import {GroupListingResponseInner, GroupsService} from "../openapi";
import {NotificationService} from "./notification.service";

@Injectable({
  providedIn: 'root'
})
export class BackendGroupService {

  groups: GroupListingResponseInner[] = []

  loadingGroups = signal(false)

  private _selectedGroup: string | undefined

  public groupUpdated = new EventEmitter<string | undefined>()

  constructor(private groupService: GroupsService, private notificationService: NotificationService) {
    this.loadGroups()

    let lsGroup = localStorage.getItem('scriptos-group')

    if (lsGroup)
      this.selectGroup(lsGroup)
  }

  loadGroups() {
    this.loadingGroups.set(true)
    this.groups = []
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

  createGroup(name: string, description: string) {
    this.groupService.createGroup({
      name, description
    }).subscribe({
      next: resp => {
        this.notificationService.success("New group", `Successfully created group '${name}'`)
        this.loadGroups()
      },
      error: err => {
        this.notificationService.displayResponse(err.error)
      }
    })
  }

  selectGroup(group: string) {
    this._selectedGroup = (this._selectedGroup == group) ? undefined : group

    if (!this._selectedGroup) {
      localStorage.removeItem('scriptos-group')
    } else {
      localStorage.setItem('scriptos-group', this._selectedGroup)
    }

    this.groupUpdated.emit(this._selectedGroup)

    // TODO Load group data?
  }

  selectedGroup(): string | undefined {
    return this._selectedGroup
  }

}
