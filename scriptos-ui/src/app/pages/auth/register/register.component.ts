import {Component, signal} from '@angular/core';
import {Button} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {FloatLabelModule} from "primeng/floatlabel";
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    Button,
    InputTextModule,
    PasswordModule,
    FloatLabelModule,
    ToastModule,
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  loading = signal(false)

  registrationData = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.pattern("[a-zA-Z0-9]+")
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8)
    ])
  })

  

  register() {

  }

}
