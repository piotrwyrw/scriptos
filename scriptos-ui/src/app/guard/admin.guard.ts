import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {SecurityService} from "../openapi";
import {catchError, map, of} from "rxjs";

export const adminGuard: CanActivateFn = (route, state) => {
  let router = inject(Router)
  return inject(SecurityService).securedAdminRoute().pipe(
    map(resp => {
      return true
    }),
    catchError(err => {
      router.navigateByUrl('/dashboard')
      return of(false)
    })
  )
};
