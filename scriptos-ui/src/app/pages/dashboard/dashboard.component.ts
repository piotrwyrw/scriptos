import {AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, signal} from '@angular/core';
import {CardModule} from "primeng/card";
import {SidebarModule} from "primeng/sidebar";
import {Button, ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {Router, RouterOutlet} from "@angular/router";
import {MenuModule} from "primeng/menu";
import {SessionService} from "../../service/session.service";
import {DialogModule} from "primeng/dialog";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {SecurityService, UserService} from "../../openapi";
import {NotificationService} from "../../service/notification.service";
import {ToastModule} from "primeng/toast";
import {ConfirmDialogComponent} from "../../component/confirm-dialog/confirm-dialog.component";
import {BackendGroupService} from "../../service/backend-group.service";
import {AuthenticationService} from "../../service/authentication.service";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CardModule,
    SidebarModule,
    Button,
    Ripple,
    RouterOutlet,
    MenuModule,
    ButtonDirective,
    DialogModule,
    FormsModule,
    InputTextModule,
    ReactiveFormsModule,
    ToastModule,
    ConfirmDialogComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements AfterViewInit, AfterContentChecked {

  private orgMenu: any[] = [
    {
      label: 'Overview',
      iconClass: 'pi pi-home',
      link: '/dashboard/overview',
      shown: true
    },
    {
      label: 'Groups',
      iconClass: 'pi pi-list',
      link: '/dashboard/groups',
      shown: true
    },
    {
      label: 'Users',
      iconClass: 'pi pi-user',
      link: '/dashboard/users',
      shown: true
    }]

  protected menu: any[] = this.orgMenu

  protected regenMenu(): void {
    let clonedMenu = Object.assign([], this.orgMenu)

    if (this.groupService.selectedGroup())
      clonedMenu.push({
        label: this.groupService.selectedGroup(),
        iconClass: 'pi pi-folder',
        link: '/dashboard/explorer',
        shown: true
      })

    this.security.securedAdminRoute().subscribe({
      next: resp => {
        clonedMenu.push({
          label: 'Administration',
          iconClass: 'pi pi-bolt',
          link: '/dashboard/admin',
          shown: true
        })
        this.menu = clonedMenu
      },
      error: err => {
        this.menu = clonedMenu
      }
    })
  }

  protected selectedEntry: string = ''

  editUserVisible = signal(false)

  editUserData = new FormGroup({
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8)
    ]),
    repeatPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8)
    ])
  })

  constructor(private router: Router, private changeDetectorRef: ChangeDetectorRef, protected sessionService: SessionService, private userService: UserService, private notificationService: NotificationService, private groupService: BackendGroupService, protected authService: AuthenticationService, protected security: SecurityService) {
    this.highlightMenuEntry()
    this.groupService.groupUpdated.subscribe({
      next: (group: string | undefined) => {
        this.updateExplorerTab()
      }
    })
    this.updateExplorerTab()
  }

  updateExplorerTab() {
    this.regenMenu()
  }

  ngAfterContentChecked(): void {
    this.highlightMenuEntry()
  }

  editUser() {
    this.editUserData.reset()
    this.editUserVisible.set(true)
  }

  commitUserEdit() {
    let newPassword = this.editUserData.controls['newPassword'].value!!
    let repeatPasswd = this.editUserData.controls['repeatPassword'].value!!
    if (newPassword != repeatPasswd) {
      this.notificationService.error("Could not update password", "The passwords don't match")
      return
    }
    this.editUserVisible.set(false)
    this.userService.editUser({
      newPassword: newPassword
    }).subscribe({
      next: resp => {
        this.notificationService.success("Success", "Password updated successfully")
      },
      error: err => {
        this.notificationService.displayResponse(err.error)
      }
    })
  }

  select(name: string) {
    this.selectedEntry = name
  }

  // Figure out where we are to show the correct menu entry
  highlightMenuEntry() {
    let location = this.router.url
    let menuOption = this.menu.find((entry: any) => entry.link === location)
    if (!menuOption)
      this.selectedEntry = 'overview'
    else
      this.selectedEntry = menuOption.label
  }

  ngAfterViewInit() {
    this.highlightMenuEntry()
  }

}
