import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, map, Observable } from 'rxjs';
import { User } from '../DTOS/User';


const changePasswordUrl = "http://localhost:9000/private/profile/changeAccountPassword";
const ChangeNameUrl ="http://localhost:9000/private/profile/changeAccountName";
const baseUrl = "http://localhost:9000/private/profile"
@Injectable({
  providedIn: 'root'
})
export class ManageAccountInfoService {
  

  constructor(private httpClient :HttpClient) { }
 
   	getUserInfoByEmail(email:string): Observable<User>{
		return this.httpClient.post<User>(baseUrl + '/getUserInfoByEmail',email);
  }
  
  	onChangePassword(data:any){
			return this.httpClient.post<any>(baseUrl+'/changeAccountPassword',null,{
				params:{
					"oldPassword":data.oldpass,
					"newPassword":data.newpass
				}
			});
		}
    
    public onChangeName(data: any){
      return this.httpClient.post<any>(ChangeNameUrl, null, {
         params: {
            newName: data.newname
                  }
          })
  }
	
}
