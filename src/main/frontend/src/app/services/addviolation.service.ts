import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Route, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddviolationService {
  private apiServerUrlManageViolation = "http://localhost:9000/violation"

  constructor(private httpClient: HttpClient) { 
      //  routerService: Router;
  }

  getAllViolations(): Observable<any> {
    return this.httpClient.get<any>(`${this.apiServerUrlManageViolation}/violations`);
  }

  // addViolation(body: any): Observable<any> {
  //   return this.httpClient.post<any>(`${this.apiServerUrlManageViolation}/addviolation`, body);
  // }
  sendViolation(violation_id: number, createdDate: Date, description: string, account_id: number): Observable<any> {
    const params = {
      violation_id: violation_id,
      createdDate: createdDate,
      description: description,
      account_id: account_id
    }
    
    return this.httpClient.post<any>(this.apiServerUrlManageViolation + "/addviolation", { params });
  }
}
