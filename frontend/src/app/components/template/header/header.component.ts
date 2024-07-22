import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import { AuthenticationService } from '../../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatIcon
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ){}

  onLogout(){
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

}
