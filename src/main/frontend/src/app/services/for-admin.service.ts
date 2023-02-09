import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { User } from '../DTOS/User';

const allInspectorsUrl = "http://localhost:9000/public/manageInspector/allInspectors";
const addInspectorUrl = "http://localhost:9000/public/manageInspector/addInspector";
const activateUrl = "http://localhost:9000/public/manageInspector/activateInspector";
const deactivateUrl = "http://localhost:9000/public/manageInspector/deactivateInspector";
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

  private apiServerUrl = "http://localhost:9000";

  constructor(private httpClient: HttpClient) { }

  getAllInspectors(): Observable<User[]>{
    return this.httpClient.get<User[]>(`${this.apiServerUrl}/public/manageInspector/allInspectors`);
  }

  addInspector(body: any): Observable<any> {
    return this.httpClient.post<any>(`${this.apiServerUrl}/public/manageInspector/addInspector`, body, httpOptions);
  }

  updateInspector(body: any): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrl}/public/manageInspector/updateInspector`, body);
  }

  deleteInspector(id: number| undefined): Observable<any>{
    return this.httpClient.delete<any>(`${this.apiServerUrl}/public/manageInspector/deleteInspector/${id}`)
  }

  activateInspector(): Observable<any>{
    return this.httpClient.put<any>(activateUrl, null);
  }

  deactivateInspector(): Observable<any>{
    return this.httpClient.put<any>(deactivateUrl, null);
  }
}
