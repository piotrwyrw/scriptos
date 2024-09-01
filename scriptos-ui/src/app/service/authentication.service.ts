import {Injectable} from '@angular/core';
import {BasicUserDataResponse, ErrorResponse, SecurityService, UserService} from "../openapi";
import {SessionService} from "./session.service";
import {catchError, map, Observable, of} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  public username: string | undefined = undefined

  constructor(private userService: UserService, private sessionService: SessionService, private securityService: SecurityService, private router: Router) {
    this.testSessionServiceToken()
  }

  private testSessionServiceToken() {
    this.testToken().subscribe(resp => {
      if (!resp) {
        this.sessionService.sessionToken = undefined
        console.log("Removed outdated or invalid session token from local storage")
        this.router.navigateByUrl('auth')
      } else {
        console.log("Session token check OK")
        this.username = resp.username
      }
    })
  }

  logIn(username: string, password: string): Observable<ErrorResponse | undefined> {
    return this.userService.userLogin({
      username, password
    }).pipe(
      map(response => {
        this.sessionService.sessionToken = response.token
        this.username = response.username
        return undefined
      }),
      catchError(err => {
        this.sessionService.sessionToken = undefined
        return of(err.error as ErrorResponse)
      })
    )
  }

  register(username: string, password: string): Observable<ErrorResponse | undefined> {
    return this.userService.userRegister({
      username, password
    }).pipe(
      map(resp => {
        return undefined
      }),
      catchError(errorResp => {
        return of(errorResp.error)
      })
    )
  }

  testToken(): Observable<BasicUserDataResponse | undefined> {
    // If there's no session token in the local storage, we'll just act as if everything was ok
    if (!this.sessionService.sessionToken)
      return of(undefined)

    return this.securityService.securedRoute().pipe(
      map(resp => {
        return resp
      }),
      catchError(error => {
        return of(undefined)
      })
    )
  }

}
