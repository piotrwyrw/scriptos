import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {SessionService} from "../service/session.service";

export const authorizationInterceptor: HttpInterceptorFn = (req, next) => {
  let sessionService = inject(SessionService)

  if (sessionService.sessionToken) {
    let newRequest = req.clone({
      setHeaders: {'Authorization': `Bearer ${sessionService.sessionToken}`}
    })
    return next(newRequest)
  }

  return next(req);
};
