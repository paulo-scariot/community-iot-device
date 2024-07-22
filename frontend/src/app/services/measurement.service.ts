import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { BaseAbstractService } from './base.abstract.service';
import { environment } from '../environments/environment';


@Injectable({providedIn: 'root'})
export class MeasurementService extends BaseAbstractService<any> {

  constructor(http: HttpClient) {
    super(http, `${environment.backend}/measurement`);
  }

}
