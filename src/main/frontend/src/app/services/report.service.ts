import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  sendReportUrl = "http://localhost:9000/report/sendReport";

  constructor(private httpClient: HttpClient) { }

  sendReport(productId: number, accountId: number, description: string, violationTypeId: number): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId,
      description: description,
      violationTypeId: violationTypeId
    }
    return this.httpClient.post<any>(this.sendReportUrl, null, {params});
  }
}
