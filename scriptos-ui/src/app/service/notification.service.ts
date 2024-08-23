import {Injectable} from '@angular/core';
import {MessageService} from "primeng/api";
import {ErrorResponse} from "../openapi";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private messageService: MessageService) {
  }

  genericNotification(title: string, description: string, type: "success" | "info" | "warn" | "error") {
    this.messageService.add({
      summary: title,
      detail: description,
      severity: type
    })
  }

  success(title: string, description: string) {
    this.genericNotification(title, description, "success")
  }

  info(title: string, description: string) {
    this.genericNotification(title, description, "info")
  }

  warning(title: string, description: string) {
    this.genericNotification(title, description, "warn")
  }

  error(title: string, description: string) {
    this.genericNotification(title, description, "error")
  }

  displayResponse(errorResponse: ErrorResponse) {
    this.error("Error", errorResponse.message ?? "No error message provided")
  }

}
