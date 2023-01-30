import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, map, Observable } from 'rxjs';
import { User } from '../DTOS/User';

const baseUrl = "http://localhost:9000/private/profile"
@Injectable({
  providedIn: 'root'
})
export class ManageAccountInfoService {
  

  constructor(private http:HttpClient) { }
 
   	getUserInfoByEmail(email:string): Observable<User> {
		
		return this.http.post<User>(baseUrl + '/getUserInfoByEmail',email);
		
  }
  
  	onChangePassword(data:any){
		
			return this.http.post<any>(baseUrl+'/changeAccountPassword',null,{
				params:{
					"oldPassword":data.oldpass,
					"newPassword":data.newpass
				}
			});
		}
		
}
