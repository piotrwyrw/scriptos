import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {PrimeNGConfig} from "primeng/api";
import {AuthenticationService} from "./service/authentication.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  constructor(private primeNgConfig: PrimeNGConfig, private authService: AuthenticationService) {
  }

  ngOnInit() {
    this.primeNgConfig.ripple = true
  }

}
