import { Routes } from '@angular/router';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { DeviceListComponent } from './views/device/device-list.component';
import { AuthGuard } from './guards/auth.guard';
import { LoginComponent } from './views/login/login.component';
import { MainComponent } from './components/template/main/main.component';
import { UserListComponent } from './views/user/user-list.component';
import { CommandListComponent } from './views/command/command-list.component';
import { DeviceComponent } from './views/device/device.component';
import { CommandComponent } from './views/command/command.component';
import { UserComponent } from './views/user/user.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', component: DashboardComponent },
      { path: 'dispositivos',
        children: [
          {path: '', component: DeviceListComponent},
          {path: 'novo', component: DeviceComponent},
          {path: ':id', component: DeviceComponent}
        ] 
      },
      { path: 'comandos',
        children: [
          {path: '', component: CommandListComponent},
          {path: 'novo', component: CommandComponent},
          {path: ':id', component: CommandComponent}
        ] 
      },
      { path: 'usuarios',
        children: [
          {path: '', component: UserListComponent},
          {path: 'novo', component: UserComponent},
          {path: ':id', component: UserComponent}
        ] 
      },
    ]
  },
  { path: '**', redirectTo: '', pathMatch: 'full' }

];
