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

  //xem 1 thằng đã report version ấy chưa
  getReportByProductVersionAndUser(productId: number, accountId: number, version: string): Observable<any>{
    const params = {
      productId: productId,
      accountId: accountId,
      version: version
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductUserVersion",{params});
  }

  //cập nhật trạng thái report
  updateReportStatus(productId: number, version: string, userIdList: number[], statusList: string[]){
    let params = new HttpParams().set('productId', productId).set('version',version).set('userIdList',userIdList.join(',')).set('statusList',statusList.join(','));
    return this.httpClient.put<any>(this.BaseUrl+"/updateReportStatus",null,{params})
  }

  //lấy các sản phẩm theo product details
  getByProductDetailsAndStatus(productId: number, version: string, status: string): Observable<any>{
    const params = {
      productId: productId,
      version: version,
      status: status
    }
    return this.httpClient.get<any>(this.BaseUrl+"/getByProductAndStatus",{params});
  }
}
