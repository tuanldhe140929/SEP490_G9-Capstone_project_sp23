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

  addViolation(account_id: number, description: string): Observable<any> {
    const params = {
      account_id: account_id,
      description: description
    }
    return this.httpClient.post<any>(`${this.apiServerUrlManageViolation}/addviolation`, null, {params});
  }
 
  updateSellerStatus(account_id: number): Observable<any>{
    const params = {
      account_id: account_id
    }
    return this.httpClient.put<any>(this.apiServerUrlManageViolation+ '/updateSellerStatus', null, {params});
  }
}
