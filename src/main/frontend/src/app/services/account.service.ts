import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

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

  constructor(private httpClient: HttpClient, private storageService: StorageService) { }

  login(body: any): Observable<any> {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount+'/login', body, httpOptions);
  }

logout() {
  localStorage.clear();
  return this.httpClient.get<any>(this.apiServerUrlManageAccount + '/logout?token=' + this.storageService.getToken(), httpOptions,);
  }
  // getAccountOfSeller()

  getAllStaffs(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageAccount}/staffs`);
  }

  addStaff(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageAccount}/addStaff`, body, {});
  }

  updateStaffStatus(id: number): Observable<any>{
    const params = {
      id: id
    }
    return this.httpClient.put<any>(this.apiServerUrlManageAccount+ '/updateStaffStatus', null, {params});
  }
  
    refreshToken() {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount + '/refresh', 1, httpOptions);
  }
  
  resetPassword(email: String) {
    return this.httpClient.post<any>(this.apiServerUrlManageAccount + '/resetPassword?email=' + email.toString(), {});
  }

  getAllAccounts(){
    return this.httpClient.get<any>(this.apiServerUrlManageAccount + '/allAccounts');
  }
}
