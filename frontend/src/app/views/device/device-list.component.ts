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
import { DeviceService } from '../../services/device.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-device-list',
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
  templateUrl: './device-list.component.html',
  styleUrl: './device-list.component.css'
})
export class DeviceListComponent implements AfterViewInit {
  displayedColumns: string[] = ['identifier', 'manufacturer', 'url', 'status', 'actions'];
  dataSource = new MatTableDataSource<Device>();
  resultsLength = 0;
  filter: string = "";

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private deviceService: DeviceService,
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
          return this.deviceService.list(this.paginator.pageIndex, this.paginator.pageSize, this.filter);
        }),
        map(data => {
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
    await this.deviceService.delete(id);
    this.list();
  }
}
