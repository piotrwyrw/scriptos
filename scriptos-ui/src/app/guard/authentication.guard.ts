import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {SessionService} from "../service/session.service";

export const authenticationGuard: CanActivateFn = (route, state) => {
  let token = inject(SessionService).sessionToken

  if (token == undefined)
    return true

  inject(Router).navigateByUrl('/dashboard')
  return false
};
