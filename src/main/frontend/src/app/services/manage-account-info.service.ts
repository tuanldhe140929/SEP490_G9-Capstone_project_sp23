import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, map, Observable } from 'rxjs';
import { User } from '../DTOS/User';

const changePasswordUrl = "http://localhost:9000/private/profile/changeAccountPassword";
const baseUrl = "http://localhost:9000/private/"
@Injectable({
  providedIn: 'root'
})
export class ManageAccountInfoService {
  

  constructor(private httpClient :HttpClient) { }
 
   	getUserInfoByEmail(email:string): Observable<User> {
		
		return this.httpClient.post<User>(baseUrl + 'profile/getUserInfoByEmail',email);
  }

	public onChangePassword(data: any){
		return this.httpClient.post<any>(changePasswordUrl + "?newPassword=" + data.newpassword + "&oldPassword=" + data.oldpassword, {
      		
      }
    )
	
}
}
