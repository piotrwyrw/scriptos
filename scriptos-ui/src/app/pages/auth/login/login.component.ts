import { Component } from '@angular/core';
import {InputTextareaModule} from "primeng/inputtextarea";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {Button} from "primeng/button";
import {FloatLabelModule} from "primeng/floatlabel";
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    InputTextareaModule,
    InputTextModule,
    PasswordModule,
    Button,
    FloatLabelModule,
    ToastModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  userData = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

}
