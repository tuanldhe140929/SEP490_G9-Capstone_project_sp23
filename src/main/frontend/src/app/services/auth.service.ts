import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
baseUrl = "http://localhost:9000/public/auth";
	
	constructor(private http: HttpClient) { }

	 login(body : any): Observable<User> {

        return this.http.post<User>(this.baseUrl + "/login", body);
    }

    register(body: any): Observable<User> {
        return this.http.post<User>(this.baseUrl + "/register", body);
    }

    loginWithGoogle(body: any): Observable<User> {
        return this.http.post<User>(this.baseUrl + "/loginWithGoogle", body);
    }
}
