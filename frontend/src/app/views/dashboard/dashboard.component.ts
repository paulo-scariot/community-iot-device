import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { map, merge, startWith, switchMap } from 'rxjs';
import { Device } from '../../models/model';
import { ActivatedRoute, Router } from '@angular/router';
import { MeasurementService } from '../../services/measurement.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-dashboard',
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
    DatePipe
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements AfterViewInit {
  displayedColumns: string[] = ['createdAt', 'device', 'command', 'result'];
  dataSource = new MatTableDataSource<Device>();
  resultsLength = 0;
  filter: string = "";

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private measurementService: MeasurementService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngAfterViewInit() {
    this.list();
  }

  list() {
    merge(this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.measurementService.list(this.paginator.pageIndex, this.paginator.pageSize, this.filter);
        }),
        map(data => {
          console.log('data', data);
          
          
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

}
