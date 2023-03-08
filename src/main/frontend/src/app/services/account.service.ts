import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private apiServerUrlManageAccount = "http://localhost:9000/account";

  constructor(private httpClient: HttpClient) { }

  getAllStaffs(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageAccount}/staffs`);
  }

  addStaff(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageAccount}/addStaff`, body);
  }

  updateStaffStatus(id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageAccount}/updateStaffStatus/${id}`, id);
  }
}
