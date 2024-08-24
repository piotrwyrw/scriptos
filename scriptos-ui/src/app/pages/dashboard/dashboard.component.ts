import {AfterViewInit, Component} from '@angular/core';
import {CardModule} from "primeng/card";
import {SidebarModule} from "primeng/sidebar";
import {Button} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {Router, RouterOutlet} from "@angular/router";
import {MenuModule} from "primeng/menu";
import {SessionService} from "../../service/session.service";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CardModule,
    SidebarModule,
    Button,
    Ripple,
    RouterOutlet,
    MenuModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements AfterViewInit {

  protected menu = [
    {
      label: 'Overview',
      iconClass: 'pi pi-home',
      link: '/dashboard/overview',
      shown: true
    },
    {
      label: 'Groups',
      iconClass: 'pi pi-list',
      link: '/dashboard/groups',
      shown: true
    },
    {
      label: 'Explorer',
      iconClass: 'pi pi-folder',
      link: '/dashboard',
      shown: false
    },
    {
      label: 'Users',
      iconClass: 'pi pi-user',
      link: '/dashboard',
      shown: true
    },
    {
      label: 'Admin',
      iconClass: 'pi pi-bolt',
      link: '/dashboard',
      shown: true
    }
  ]

  protected selectedEntry: string = ''

  select(name: string) {
    this.selectedEntry = name
  }

  constructor(private router: Router, protected sessionService: SessionService) {
    this.highlightMenuEntry()
  }

  // Figure out where we are to show the correct menu entry
  highlightMenuEntry() {
    let location = this.router.url
    let menuOption = this.menu.find(entry => entry.link === location)
    if (!menuOption)
      this.selectedEntry = 'overview'
    else
      this.selectedEntry = menuOption.label
  }

  ngAfterViewInit() {
    this.highlightMenuEntry()
  }

}
