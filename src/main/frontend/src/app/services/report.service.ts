import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  BaseUrl = "http://localhost:9000/report";

  constructor(private httpClient: HttpClient) { }

  sendReport(productId: number, accountId: number, description: string, violationTypeId: number): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId,
      description: description,
      violationTypeId: violationTypeId
    }
    return this.httpClient.post<any>(this.BaseUrl+"/sendReport", null, {params});
  }

  getReportByProductAndUser(productId: number, accountId: number): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductAndUser",{params});
  }
}
