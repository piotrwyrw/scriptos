import {Injectable} from '@angular/core';
import {ErrorResponse, UserService} from "../openapi";
import {SessionService} from "./session.service";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    constructor(private userService: UserService, private sessionService: SessionService) {
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

}
