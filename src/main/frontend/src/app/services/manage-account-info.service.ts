import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, map, Observable } from 'rxjs';
import { User } from '../interfaces/User';

const baseUrl = "http://localhost:9000/private/"
@Injectable({
  providedIn: 'root'
})
export class ManageAccountInfoService {
  

  constructor(private http:HttpClient) { }
 
   	getUserInfoByEmail(email:string): Observable<User> {
		
		return this.http.post<User>(baseUrl + 'profile/getUserInfoByEmail',email);
  }

	
	
}
