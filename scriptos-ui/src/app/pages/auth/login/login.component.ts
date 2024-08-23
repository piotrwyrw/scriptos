import {Component, signal} from '@angular/core';
import {InputTextareaModule} from "primeng/inputtextarea";
import {InputTextModule} from "primeng/inputtext";
import {PasswordModule} from "primeng/password";
import {Button} from "primeng/button";
import {FloatLabelModule} from "primeng/floatlabel";
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {AuthenticationService} from "../../../service/authentication.service";
import {NotificationService} from "../../../service/notification.service";
import {Router} from "@angular/router";

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
    ReactiveFormsModule,
    ProgressSpinnerModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loading = signal(false)

  loginData = new FormGroup({
    username: new FormControl("", Validators.required),
    password: new FormControl("", Validators.required)
  })

  constructor(private authenticationService: AuthenticationService, private notificationService: NotificationService, private router: Router) {
  }

  resetForm() {
    this.loginData.reset()
  }

  login() {
    this.loading.set(true)
    this.authenticationService.logIn(this.loginData.controls['username'].value!, this.loginData.controls['password'].value!)
      .subscribe((error) => {
        if (!error) {
          this.notificationService.success("Logged In", "The log in was successful")
          setTimeout(() => {
            this.router.navigateByUrl('dashboard')
          }, 500)
        } else {
          this.notificationService.displayResponse(error)
          this.loading.set(false)
          this.resetForm()
        }
      })
  }

}
