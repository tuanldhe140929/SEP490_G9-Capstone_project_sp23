import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

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
export class AccountService {

  private apiServerUrlManageAccount = "http://localhost:9000/account";

  constructor(private httpClient: HttpClient) { }

  login(body: any): Observable<any> {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount+'/login', body, httpOptions);
  }

logout() {
    localStorage.clear();
    return this.httpClient.get<any>(this.apiServerUrlManageAccount + '/logout', httpOptions);
  }
  
  getAllStaffs(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageAccount}/staffs`);
  }

  addStaff(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageAccount}/addStaff`, body);
  }

  updateStaffStatus(id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageAccount}/updateStaffStatus/${id}`, id);
  }
  
    refreshToken() {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount + '/refresh', 1, httpOptions);
  }
  
  resetPassword(email: String) {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount + '/resetPassword?email=' + email.toString(), {});
  }
}
