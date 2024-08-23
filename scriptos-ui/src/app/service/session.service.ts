import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private _token: string | undefined = undefined

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

}
