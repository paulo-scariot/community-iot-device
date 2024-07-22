import { Component, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, FormGroupName, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { DeviceService } from '../../services/device.service';
import { UserService } from '../../services/user.service';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    MatCardModule,
    MatPaginatorModule,
    MatPaginator,
    MatTableModule,
    MatCheckbox,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatLabel,
    MatInput,
    MatSelect,
    FormsModule,
    ReactiveFormsModule,
    MatOption
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  id: number;
  hide = signal(true);

  public formGroup = new FormGroup({
    id: new FormControl(null),
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
  });

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }
  
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get("id"));
    this.userService.get(this.id)
      .then(res => {
        console.log('res', res);
        
        if (res){
          this.formGroup.patchValue(res);
        }
      });
    
  }
  
  close(){
    this.router.navigate(['..'], {relativeTo: this.route});
  }

  async save(){
    if (this.id !== 0){
      await this.userService.put(this.formGroup.value, this.id)
    } else {
      await this.userService.save(this.formGroup.value)
    }
    this.close();
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
}
