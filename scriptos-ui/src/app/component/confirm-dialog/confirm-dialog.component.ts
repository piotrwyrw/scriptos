import {Component, EventEmitter, Input, Output, signal} from '@angular/core';
import {Button} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    Button,
    DialogModule,
    FormsModule,
    InputTextModule,
    ReactiveFormsModule
  ],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss'
})
export class ConfirmDialogComponent {

  protected visible = signal(false)

  @Input() description: string = "Do you want to proceed?"
  @Output() perform = new EventEmitter<void>()

  show() {
    this.visible.set(true)
  }

  hide() {
    this.visible.set(false)
  }

}
