import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private snackBar: MatSnackBar,
    private authenticationService: AuthenticationService,
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
    const credentials = localStorage.getItem('credentials');
    if (credentials) {
      request = request.clone({
        setHeaders: {
          Authorization: `Basic ${credentials}`
        }
      });
    }
    return next.handle(request).pipe(
      tap( event => this.successResponseAction(event)),
      catchError(error => this.errorResponseAction(error))
    );
  }

  private errorResponseAction(error: any) {   

    let msg = error.error.message;

    if (!msg && error.status === 500) {
      msg = "Unknown error";
    }

    const credentials = localStorage.getItem('credentials');
    const isAuthError = error.status === 401 || error.status === 403;
    if (isAuthError && !credentials) {
      this.authenticationService.logout();

      if (!msg) {
        msg = 'Unauthorized';
      }
    }

    if (msg) {
      this.showErrorMessage(msg);
    }
    return throwError(() => error);
  }

  private successResponseAction(success: any) {
    // console.log('success', success);

    let msg = success.body ? success.body.message : null;
    if (msg) {
      this.showSuccessMessage(msg);
    }
  }

  showErrorMessage(msg: string) {
    let config = new MatSnackBarConfig();
    config.panelClass = ['error-message'];
    this.snackMsg(msg, config);
  }

  showSuccessMessage(msg: string) {
    let config = new MatSnackBarConfig();
    config.panelClass = ['success-message'];
    this.snackMsg(msg, config);
  }

  private snackMsg(msg: string, config: MatSnackBarConfig) {
    config.duration = 3000;
    config.horizontalPosition = 'left';
    this.snackBar.open(msg, '', config);
  }
}