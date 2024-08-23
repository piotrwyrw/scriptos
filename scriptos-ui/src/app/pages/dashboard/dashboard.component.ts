import { Component } from '@angular/core';
import {BackendGroupService} from "../../service/backend-group.service";
import {CardModule} from "primeng/card";
import {SidebarModule} from "primeng/sidebar";
import {Button} from "primeng/button";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CardModule,
    SidebarModule,
    Button
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

  constructor(protected backendGroupService: BackendGroupService) {
  }

  loadGroups() {
    this.backendGroupService.loadGroups()
  }

}
