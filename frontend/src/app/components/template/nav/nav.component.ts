import { Component } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { DashboardComponent } from '../../../views/dashboard/dashboard.component';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
     MatSidenavModule,
     MatListModule,
     DashboardComponent,
     RouterModule
    ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {

}
