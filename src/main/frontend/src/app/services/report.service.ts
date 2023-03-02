import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  addReportUrl = "http://localhost:9000/private/manageReport/add";

  constructor(private httpClient: HttpClient) { }

  addReport(body: any): Observable<any>{
    return this.httpClient.post<any>(this.addReportUrl, body);
  }
}
