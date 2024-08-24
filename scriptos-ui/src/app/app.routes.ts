import {Routes} from '@angular/router';
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {AuthComponent} from "./pages/auth/auth.component";
import {tokenGuard} from "./guard/token.guard";
import {OverviewComponent} from "./pages/dashboard/overview/overview.component";
import {GroupsComponent} from "./pages/dashboard/groups/groups.component";
import {authenticationGuard} from "./guard/authentication.guard";

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthComponent,
    canActivate: [authenticationGuard]
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [tokenGuard],
    children: [
      {
        path: '',
        redirectTo: 'overview',
        pathMatch: 'full'
      },
      {
        path: 'overview',
        component: OverviewComponent
      },
      {
        path: 'groups',
        component: GroupsComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard/overview'
  }
];
