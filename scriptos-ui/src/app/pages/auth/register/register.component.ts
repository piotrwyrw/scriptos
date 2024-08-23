import {Component, signal} from '@angular/core';
import {Button} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {FloatLabelModule} from "primeng/floatlabel";
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {AuthenticationService} from "../../../service/authentication.service";
import {NotificationService} from "../../../service/notification.service";

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

  constructor(private authService: AuthenticationService, private notificationService: NotificationService) {
  }

  resetForm() {
    this.registrationData.reset()
  }

  register() {
    this.loading.set(true)
    this.authService.register(this.registrationData.controls['username'].value!, this.registrationData.controls['password'].value!)
      .subscribe(error => {
        this.loading.set(false)
        if (!error) {
          this.notificationService.success("Registered", "Registration successful")
        } else {
          this.notificationService.displayResponse(error)
        }
        this.resetForm()
      })
  }

}
