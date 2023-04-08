import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddviolationService {
  private apiServerUrlManageViolation = "http://localhost:9000/violation"

  constructor(private httpClient: HttpClient) { }

  getAllViolations(): Observable<any>{
    return this.httpClient.get<any>(`${this.apiServerUrlManageViolation}/violations`);
  }

  addViolation(body: any): Observable<any>{
    return this.httpClient.post<any>(`${this.apiServerUrlManageViolation}/addviolation`,body);
  }

}
