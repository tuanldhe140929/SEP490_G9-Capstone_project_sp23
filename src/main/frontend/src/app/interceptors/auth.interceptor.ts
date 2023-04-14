import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpResponse,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, of, throwError } from 'rxjs';
import { StorageService } from '../services/storage.service';
import { Router } from '@angular/router';
import { AccountService } from '../services/account.service';
const TOKEN_HEADER_KEY = 'Authorization';
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private storage: StorageService, private router:Router, private accountService:AccountService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
     	let authReq = req;
     	if(this.storage.getToken()!=null){
    	const token = this.storage.getToken();
          authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
    }
    return next.handle(authReq).pipe(catchError(x => this.handleAuthError(x)));
}

private handleAuthError(err: HttpErrorResponse): Observable<any> {
  if (err.status === 401) {

    this.storage.clearStorage();
    this.accountService.refreshToken().subscribe(
      response => {
        this.storage.saveUser(response.body);
        this.storage.saveToken(response.body.accessToken);
        location.reload();
      }, error => {
        console.log(error);
        this.router.navigateByUrl(`login`);
      });

    return of(err.message);
  } else if (err.status === 0) {
    this.router.navigateByUrl('error');
  }
        return throwError(err);
  }

  doNothing() {

  }

  authCheck() {
  }
}


