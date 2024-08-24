import {Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {NotificationService} from "./notification.service";
import {UserService} from "../openapi";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private _token: string | undefined = undefined

  constructor(private router: Router, private notificationService: NotificationService, private userService: UserService) {
  }

  public get sessionToken(): string | undefined {
    if (!this._token) {
      let lsToken = localStorage.getItem('scriptos-session')
      if (lsToken) {
        this.sessionToken = lsToken
      }
    }

    return this._token
  }

  public set sessionToken(token: string | undefined) {
    this._token = token
    if (!token)
      localStorage.removeItem('scriptos-session')
    else
      localStorage.setItem('scriptos-session', token)
  }

  dropSession() {
    this.userService.dropSession().subscribe({
      next: resp => {
        this.sessionToken = undefined
        this.router.navigateByUrl('/auth')
      },
      error: err => {
        this.notificationService.error("Could not log out", "Logout failed with an unknown error")
      }
    })
  }

}
