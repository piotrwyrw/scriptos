import {Routes} from '@angular/router';
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {AuthComponent} from "./pages/auth/auth.component";
import {tokenGuard} from "./guard/token.guard";
import {OverviewComponent} from "./pages/dashboard/overview/overview.component";
import {GroupsComponent} from "./pages/dashboard/groups/groups.component";
import {authenticationGuard} from "./guard/authentication.guard";
import {ExplorerComponent} from "./pages/dashboard/explorer/explorer.component";
import {UsersComponent} from "./pages/dashboard/users/users.component";
import {AdminComponent} from "./pages/dashboard/admin/admin.component";
import {adminGuard} from "./guard/admin.guard";

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
      },
      {
        path: 'explorer',
        component: ExplorerComponent
      },
      {
        path: 'users',
        component: UsersComponent
      },
      {
        path: 'admin',
        component: AdminComponent,
        canActivate: [adminGuard]
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard/overview'
  }
];
