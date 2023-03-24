import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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

  getByStatus(status: string): Observable<any>{
    const params = {
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByStatus",{params})
  }

  updateReportStatus(productId: number, userIdList: number[], statusList: string[]){
    let params = new HttpParams().set('productId', productId).set('userIdList',userIdList.join(',')).set('statusList',statusList.join(','));
    return this.httpClient.put<any>(this.BaseUrl+"/updateReportStatus",null,{params})
  }

  getAllReports(){
    return this.httpClient.get<any>(this.BaseUrl+"/getAllReports");
  }

  getByProductAndStatus(productId: number, status: string){
    const params = {
      productId: productId,
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductAndStatus",{params});
  }
}
