import { Component } from '@angular/core';
import {BackendGroupService} from "../../service/backend-group.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-explorer',
  standalone: true,
  imports: [],
  templateUrl: './explorer.component.html',
  styleUrl: './explorer.component.scss'
})
export class ExplorerComponent {

  constructor(protected backendGroupService: BackendGroupService, private router: Router) {
    if (!backendGroupService.selectedGroup())
      router.navigateByUrl('/dashboard/groups')
  }

}
