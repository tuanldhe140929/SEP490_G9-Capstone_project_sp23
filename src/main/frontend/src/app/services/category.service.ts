import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiServerUrlManageCategory = "http://localhost:9000/private/manageCategory";

  constructor(private httpClient: HttpClient) { }

  getAllCategories(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageCategory}/categories`);
  }

  addCategory(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageCategory}/addCategory`,body);
  }

  updateCategory(body: any, id: number): Observable<any>{
    return this.httpClient.put<any>(`${this.apiServerUrlManageCategory}/updateCategory/${id}`,body);
  }
}
