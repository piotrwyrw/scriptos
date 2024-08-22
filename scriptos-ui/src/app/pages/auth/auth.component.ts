import { Component } from '@angular/core';
import {CardModule} from "primeng/card";
import {TabViewModule} from "primeng/tabview";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    CardModule,
    TabViewModule,
    LoginComponent,
    RegisterComponent,
    ToastModule
  ],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {

}
