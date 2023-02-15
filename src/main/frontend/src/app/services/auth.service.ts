import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from '../DTOS/AuthResponse';
import { User } from '../DTOS/User';


const baseUrl = "http://localhost:9000/public/auth";


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
  sendVerifyEmail(email: string) {
    return this.http.get(baseUrl + '/sendVerifyEmail', {
      params: {
        email: email
      }
    });
  }
  verifyEmail(verifyLink: string | null, email: string) {
    return this.http.get(baseUrl + '/verifyEmail/' + verifyLink, {
      params: {
        email: email
      }
    });
  }

  static isLoggedIn: boolean;

  authResponse: AuthResponse | undefined;

  set AuthResponse(auth: AuthResponse) {
    this.authResponse = auth;
  }

  get AuthResponset() {
    return this.authResponse;
  }
  constructor(private http: HttpClient) { }

  login(body: any): Observable<any> {
    return this.http.post<any>(baseUrl + '/login', body, httpOptions);
  }

  register(body: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(baseUrl + "/register", body);
  }

  loginWithGoogle(body: any): Observable<any> {
    return this.http.post<any>(baseUrl + "/loginWithGoogle", body, httpOptions);
  }

  logout() {
    localStorage.clear();
    return this.http.get<any>(baseUrl + '/logout', httpOptions);
  }
  refreshToken() {
    return this.http.post<any>(baseUrl + '/refreshToken', 1, httpOptions);
  }

  resetPasswordRequest(email: String) {
    return this.http.post<any>(baseUrl + '/resetPassword?email=' + email.toString(), {});
  }



}
