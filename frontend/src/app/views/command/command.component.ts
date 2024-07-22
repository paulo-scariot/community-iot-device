import { Component, OnInit } from '@angular/core';
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
import { CommandService } from '../../services/command.service';

@Component({
  selector: 'app-command',
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
    ReactiveFormsModule
  ],
  templateUrl: './command.component.html',
  styleUrl: './command.component.css'
})
export class CommandComponent implements OnInit {

  id: number;

  public formGroup = new FormGroup({
    id: new FormControl(null),
    command: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });

  constructor(
    private commandService: CommandService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }
  
  ngOnInit(): void {    
    this.id = Number(this.route.snapshot.paramMap.get("id"));
    this.commandService.get(this.id)
      .then(res => {
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
      await this.commandService.put(this.formGroup.value, this.id)
    } else {
      await this.commandService.save(this.formGroup.value)
    }
    this.close();
  }

}
