import {Injectable, signal} from '@angular/core';
import {DocumentListingResponseInner, DocumentService} from "../openapi";
import {BackendGroupService} from "./backend-group.service";
import {NotificationService} from "./notification.service";

@Injectable({
  providedIn: 'root'
})
export class BackendDocumentService {

  documents: DocumentListingResponseInner[] = []

  loadingDocuments = signal(false)

  constructor(private documentService: DocumentService, private groupService: BackendGroupService, private notificationService: NotificationService) {
  }

  loadDocumentListing() {
    this.loadingDocuments.set(true)

    if (!this.groupService.selectedGroup()) {
      this.documents = []
      this.loadingDocuments.set(false)
      return
    }

    this.documentService.getAllDocuments(this.groupService.selectedGroup()!!).subscribe({
      next: resp => {
        this.documents = resp
        this.loadingDocuments.set(false)
      },
      error: err => {
        this.notificationService.displayResponse(err)
        this.documents = []
        this.loadingDocuments.set(false)
      }
    })
  }

}
