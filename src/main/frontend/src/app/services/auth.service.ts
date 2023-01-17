import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from '../interfaces/AuthResponse';


const baseUrl = "http://localhost:9000/public/auth";

const httpOptions : Object = {
      headers: new HttpHeaders({
        "Content-Type": "application/json"
      }),
      observe: 'response',
      withCredentials:true
    };
@Injectable({
  providedIn: 'root'
})
export class AuthService {
    isLoggedIn: any;
  
	constructor(private http: HttpClient) { }
	
     login(body:any): Observable<any> {
    return this.http.post<any>( baseUrl+'/login',body,httpOptions);
  }
    
    

    register(body: any): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(baseUrl + "/register", body,httpOptions);
    }

    loginWithGoogle(body: any): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(baseUrl + "/loginWithGoogle", body,httpOptions);
    }
    
    refreshToken(){
		 return this.http.post<any>( baseUrl+'/refreshToken',1,httpOptions);
	}
    

}
