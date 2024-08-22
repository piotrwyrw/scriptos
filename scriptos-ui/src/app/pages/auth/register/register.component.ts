import {Component} from '@angular/core';
import {Button} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {FloatLabelModule} from "primeng/floatlabel";
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    Button,
    InputTextModule,
    PasswordModule,
    FloatLabelModule,
    ToastModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  userData = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

}
