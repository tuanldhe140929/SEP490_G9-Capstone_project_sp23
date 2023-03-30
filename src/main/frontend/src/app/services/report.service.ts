import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  BaseUrl = "http://localhost:9000/report";

  constructor(private httpClient: HttpClient) { }

  sendReport(productId: number, accountId: number, version: string, description: string, violationTypeId: number): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId,
      version: version,
      description: description,
      violationTypeId: violationTypeId
    }
    return this.httpClient.post<any>(this.BaseUrl+"/sendReport", null, {params});
  }

  getReportByProductUserVersion(productId: number, accountId: number, version: string): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId,
      version: version
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductUserVersion",{params});
  }

  getByStatus(status: string): Observable<any>{
    const params = {
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByStatus",{params})
  }

  updateReportStatus(productId: number, version: string, userIdList: number[], statusList: string[]){
    let params = new HttpParams().set('productId', productId).set('version',version).set('userIdList',userIdList.join(',')).set('statusList',statusList.join(','));
    return this.httpClient.put<any>(this.BaseUrl+"/updateReportStatus",null,{params})
  }

  getAllReports(){
    return this.httpClient.get<any>(this.BaseUrl+"/getAllReports");
  }

  getByProductAndStatus(productId: number, version: string, status: string){
    const params = {
      productId: productId,
      version: version,
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductAndStatus",{params});
  }

  getByProductsAllVersions(productId: number, status: string){
    const params = {
      productId: productId,
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByAllVersionsAndStatus",{params});
  }
}
