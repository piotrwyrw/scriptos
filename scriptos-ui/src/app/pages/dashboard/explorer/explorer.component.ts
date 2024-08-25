import {Component} from '@angular/core';
import {BackendGroupService} from "../../../service/backend-group.service";
import {Router} from "@angular/router";
import {BackendDocumentService} from "../../../service/backend-document.service";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {ChipModule} from "primeng/chip";
import {Ripple} from "primeng/ripple";
import {FilesizeService} from "../../../service/filesize.service";
import {TooltipModule} from "primeng/tooltip";

@Component({
  selector: 'app-explorer',
  standalone: true,
  imports: [
    ProgressSpinnerModule,
    ChipModule,
    Ripple,
    TooltipModule
  ],
  templateUrl: './explorer.component.html',
  styleUrl: './explorer.component.scss'
})
export class ExplorerComponent {

  constructor(protected backendGroupService: BackendGroupService, private router: Router, protected backendDocumentService: BackendDocumentService, protected fileSizeService: FilesizeService) {
    if (!backendGroupService.selectedGroup())
      this.router.navigateByUrl('/dashboard/groups')

    backendDocumentService.loadDocumentListing()
  }

}
