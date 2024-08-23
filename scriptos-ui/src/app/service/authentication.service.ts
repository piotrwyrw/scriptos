import {Injectable} from '@angular/core';
import {ErrorResponse, SecurityService, UserService} from "../openapi";
import {SessionService} from "./session.service";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    constructor(private userService: UserService, private sessionService: SessionService, private securityService: SecurityService) {
        this.testSessionServiceToken()
    }

    private testSessionServiceToken() {
        this.testToken().subscribe(resp => {
            if (!resp) {
                this.sessionService.sessionToken = undefined
                console.log("Removed outdated or invalid session token from local storage")
            }
        })
    }

    logIn(username: string, password: string): Observable<ErrorResponse | undefined> {
        return this.userService.userLogin({
            username, password
        }).pipe(
            map(tokenResponse => {
                this.sessionService.sessionToken = tokenResponse.token
                return undefined
            }),
            catchError(errorResponse => {
                this.sessionService.sessionToken = undefined
                return of(errorResponse.error as ErrorResponse)
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

    testToken(): Observable<boolean> {
        // If there's no session token in the local storage, we'll just act as if everything was ok
        if (!this.sessionService.sessionToken)
            return of(true)

        return this.securityService.securedRoute().pipe(
            map(resp => {
                return true
            }),
            catchError(error => {
                return of(false)
            })
        )
    }

}
