import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private _token: string | undefined = undefined

  public get sessionToken(): string | undefined {
    return this._token
  }

  public set sessionToken(token: string | undefined) {
    this._token = token
  }

}
