import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from '../DTOS/AuthResponse';
import { User } from '../DTOS/User';


const accountController = "http://localhost:9000/account";
const userController = "http://localhost:9000/user"
const refreshTokenController = "http://localhost:9000/refreshToken";
const httpOptions: Object = {
  headers: new HttpHeaders({
    "Content-Type": "application/json"
  }),
  observe: 'response',
  withCredentials: true
};
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  getCurrentLogedInUser(): Observable<User> {
    return this.http.get<User>(userController + '/getCurrentUser');
  }
  sendVerifyEmail(email: string) {
    return this.http.get(userController + '/sendVerifyEmail', {
      params: {
        email: email
      }
    });
  }
  verifyEmail(verifyLink: string | null, email: string) {
    return this.http.get(userController + '/verifyEmail/' + verifyLink, {
      params: {
        email: email
      }
    });
  }

  constructor(private http: HttpClient) { }

  login(body: any): Observable<any> {
    return this.http.post<any>(accountController + '/login', body, httpOptions);
  }

  register(body: any): Observable<string> {
    return this.http.post<string>(userController + "/register", body, {
      headers: new HttpHeaders({
        "Content-Type": "text"
      }),
      withCredentials: true
    });
  }

  loginWithGoogle(body: any): Observable<any> {
    return this.http.post<any>(userController + "/loginWithGoogle", body, httpOptions);
  }

  logout() {
    localStorage.clear();
    return this.http.get<any>(accountController + '/logout', httpOptions);
  }
  refreshToken() {
    return this.http.post<any>(refreshTokenController + '/refresh', 1, httpOptions);
  }

  resetPassword(email: String) {
    return this.http.post<any>(accountController + '/resetPassword?email=' + email.toString(), {});
  }
}
