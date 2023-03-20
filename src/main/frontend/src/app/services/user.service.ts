import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../DTOS/User';

const baseUrl = "http://localhost:9000/user";
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

export class UserService {

  constructor(private httpClient: HttpClient) { }

  getUserById(id: number): Observable<any>{
    return this.httpClient.get<any>(`${baseUrl}/getUserById/${id}`);
  }
  
    public onChangeName(data: any) {
    return this.httpClient.post<any>(baseUrl+'/changeAccountInfo', null, {
      params: {
        newUserName: data.newUsername,
        newFirstName: data.newFirstName,
        newLastName: data.newLastName
      }
    })
  }
  
  getCurrentUserInfo(): Observable<User> {
    return this.httpClient.get<User>(baseUrl+'/getUserInfo');
  }

  onChangePassword(data: any) {
    return this.httpClient.post<any>(baseUrl + '/changeAccountPassword', null, {
      params: {
        "oldPassword": data.oldpass,
        "newPassword": data.newpass
      }
    });
  }
  
  uploadProfileImage(file: File): Observable<any>{
    const formData = new FormData();
    formData.append("profileImage", file);
    return this.httpClient.post(baseUrl + "/uploadProfileImage", formData,{
    responseType:'text'
    });
  }
  
  sendVerifyEmail(email: string) {
    return this.httpClient.get(baseUrl + '/sendVerifyEmail', {
      params: {
        email: email
      }
    });
  }
  verifyEmail(verifyLink: string | null, email: string) {
    return this.httpClient.get(baseUrl + '/verifyEmail/' + verifyLink, {
      params: {
        email: email
      }
    });
  }
  register(body: any): Observable<string> {

    return this.httpClient.post<string>(baseUrl+"/register", body);

  }
  
  loginWithGoogle(body: any): Observable<any> {
    return this.httpClient.post<any>(baseUrl + "/loginWithGoogle", body, httpOptions);
  }
}
