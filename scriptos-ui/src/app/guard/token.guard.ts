import {CanActivateFn} from '@angular/router';
import {inject} from "@angular/core";
import {SessionService} from "../service/session.service";

export const tokenGuard: CanActivateFn = (route, state) => {
  return inject(SessionService).sessionToken != undefined;
};
