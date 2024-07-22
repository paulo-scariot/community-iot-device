import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, FormGroupName, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
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
import { Command } from '../../models/model';
import { MatOption, MatSelect } from '@angular/material/select';
import { CommandService } from '../../services/command.service';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-device',
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
    FormsModule,
    ReactiveFormsModule,
    MatSelect,
    MatOption,
    NgFor
  ],
  templateUrl: './device.component.html',
  styleUrl: './device.component.css'
})
export class DeviceComponent implements OnInit {

  id: number;
  commandList: Command[] = [];

  public formGroup = new FormGroup({
    id: new FormControl(null),
    identifier: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    status: new FormControl('', [Validators.required]),
    manufacturer: new FormControl('', [Validators.required]),
    url: new FormControl('', [Validators.required]),
    commands: new FormControl([]),
  });

  constructor(
    private deviceService: DeviceService,
    private commandService: CommandService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }
  
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get("id"));
    this.commandService.listAutoComplete().then(res => {      
      this.commandList = res.content;
    });
    this.deviceService.get(this.id)
      .then(res => {
        
        if (res){
          this.formGroup.markAsUntouched();
          this.formGroup.patchValue(res);
        }
      });
  }
  
  close(){
    this.router.navigate(['..'], {relativeTo: this.route});
  }

  async save(){
    console.log('form', this.formGroup.value);
    
    if (this.id !== 0){
      await this.deviceService.put(this.formGroup.value, this.id)
    } else {
      await this.deviceService.save(this.formGroup.value)
    }
    this.close();
  }

  compareWith(object1: any, object2: any){
    return object1 && object2 && object1.id === object2.id;
  }

}
