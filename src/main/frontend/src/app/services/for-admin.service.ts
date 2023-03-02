import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, Subject, tap } from 'rxjs';
import { Account } from '../DTOS/Account';
import { User } from '../DTOS/User';

// import * as socketIo from 'socket.io-client'

import * as socketIo from 'socket.io-client'
import { Category } from '../DTOS/Category';




const allInspectorsUrl = "http://localhost:9000/public/manageInspector/allInspectors";
const addInspectorUrl = "http://localhost:9000/public/manageInspector/addInspector";
const activateUrl = "http://localhost:9000/public/manageInspector/activateInspector";
const deactivateUrl = "http://localhost:9000/public/manageInspector/deactivateInspector";
const baseUrl = "http://localhost:9000/private/manageStaff/";
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
export class ForAdminService {

  private _refresh$ = new Subject<void>();
  private apiServerUrlManageStaff = "http://localhost:9000/account";
  private apiServerUrlManageCategory = "http://localhost:9000/private/manageCategory";
  private apiServerUrlManageTag = "http://localhost:9000/private/manageTag"

  get refresh$(){
    return this._refresh$;
  }

  constructor(private httpClient: HttpClient) { }

  getAllStaffs(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageStaff}/staffs`);
  }

  addStaff(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageStaff}/addStaff`, body);
  }

  updateStaffStatus(id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageStaff}/updateStaffStatus/${id}`, id);
  }

  getAllCategories(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageCategory}/categories`);
  }

  addCategory(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageCategory}/addCategory`,body);
  }

  updateCategory(body: any, id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageCategory}/updateCategory/${id}`,body);
  }

  getAllTags(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageTag}/tags`);
  }

  addTag(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageTag}/addTag`,body);
  }

  updateTag(body: any, id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageTag}/updateTag/${id}`,body);
  }
}
