import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from '../interfaces/AuthResponse';
import { User } from '../interfaces/User';


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
    
    static isLoggedIn:boolean;
  	
	authResponse:AuthResponse|undefined;
	
	set AuthResponse(auth:AuthResponse){
		this.authResponse = auth;
	}
	
	get AuthResponset(){
		return this.authResponse;
	}
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
    
    logout(){
		sessionStorage.clear();
		return this.http.get<any>(baseUrl + '/logout',httpOptions);
	}
    refreshToken(){
		 return this.http.post<any>( baseUrl+'/refreshToken',1,httpOptions);
	}
	
	resetPasswordRequest(email: String) {
        return this.http.post<any>(baseUrl+'/forgotAndResetPassword?email='+email.toString(),{});
    }
	
	resetPassword(body:any, email:String){
		console.log(body.captcha);
		return this.http.post<any>(baseUrl+'/forgotAndResetPasswordConfirm?email='+email.toString()+"&captcha="
		+body.captcha+"&newPassword="+body.newPassword,{
		});
	}
    

}
