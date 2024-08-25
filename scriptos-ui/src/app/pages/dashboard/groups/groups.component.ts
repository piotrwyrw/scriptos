import {AfterViewInit, Component, EventEmitter, Output, signal} from '@angular/core';
import {BackendGroupService} from "../../../service/backend-group.service";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {NgIf} from "@angular/common";
import {CardModule} from "primeng/card";
import {Button} from "primeng/button";
import {ChipModule} from "primeng/chip";
import {Ripple} from "primeng/ripple";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-groups',
  standalone: true,
  imports: [
    ProgressSpinnerModule,
    NgIf,
    CardModule,
    Button,
    ChipModule,
    Ripple,
    DialogModule,
    InputTextModule,
    ReactiveFormsModule
  ],
  templateUrl: './groups.component.html',
  styleUrl: './groups.component.scss'
})
export class GroupsComponent implements AfterViewInit {

  newGroupDialogVisible = signal(false)

  newGroupForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z]+(?:[_\\-][a-zA-Z]+)*')
    ]),
    description: new FormControl('', Validators.required)
  })

  constructor(protected groupService: BackendGroupService) {
  }

  newGroup() {
    this.newGroupForm.reset()
    this.newGroupDialogVisible.set(true)
  }

  commitNewGroupRequest() {
    this.newGroupDialogVisible.set(false)
    this.groupService.createGroup(this.newGroupForm.controls['name'].value!, this.newGroupForm.controls['description'].value!)
  }

  ngAfterViewInit() {
    this.loadGroups()
  }

  loadGroups() {
    this.groupService.loadGroups()
  }

  selectGroup(group: string) {
    this.groupService.selectGroup(group)
  }

}
