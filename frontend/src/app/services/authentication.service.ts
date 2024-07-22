import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.post<any>(`${environment.backend}/user/login`, { username, password })
            .pipe(map(res => {                
                if (res.credentials) {
                    localStorage.setItem('credentials', res.credentials);
                }
                return res;
            }));
    }

    logout() {
        localStorage.removeItem('credentials');
    }
}