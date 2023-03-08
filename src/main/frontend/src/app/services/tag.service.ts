import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private apiServerUrlManageTag = "http://localhost:9000/private/manageTag"

  constructor(private httpClient: HttpClient) { }

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
