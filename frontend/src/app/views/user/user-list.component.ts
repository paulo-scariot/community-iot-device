import { Component, ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { User } from '../../models/model';
import { FormsModule } from '@angular/forms';
import { MatInput, MatLabel } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { map, merge, startWith, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-list',
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
    FormsModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {

  displayedColumns: string[] = ['username', 'role', 'actions'];
  dataSource = new MatTableDataSource<User>();
  resultsLength = 0;
  filter: string = "";

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,){}

  ngAfterViewInit() {
    this.list();
  }

  list() {
    merge(this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.userService.list(this.paginator.pageIndex, this.paginator.pageSize, this.filter);
        }),
        map(data => {
          console.log('data',data);
          
          if (data === null) {
            return [];
          }
          this.resultsLength = data.totalElements;
          return data.content;
        }),
      )
      .subscribe(data => (this.dataSource.data = data));
  }

  applyFilter() {
    this.list();
  }

  create(){
    this.router.navigate(['novo'], {relativeTo: this.route});
  }

  open(id: number) {    
    this.router.navigate([id], {relativeTo: this.route});
  }

  async delete(id: number) {
    await this.userService.delete(id);
    this.list();
  }
}
