import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../DTOS/User';

const allInspectorsUrl = "http://localhost:9000/public/manageInspector/allInspectors"
const addInspectorUrl = "http://localhost:9000/public/manageInspector/addInspector";
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
export class ManageInspectorService {

  constructor(private httpClient: HttpClient) { }

  getAllInspectors(): Observable<any>{
    return this.httpClient.get<any>(allInspectorsUrl);
  }

  addInspector(body: any): Observable<any> {
    return this.httpClient.post<any>(addInspectorUrl, body, httpOptions);
  }
}
