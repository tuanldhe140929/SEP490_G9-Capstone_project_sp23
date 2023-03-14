import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const baseUrl = "http://localhost:9000/seller"
@Injectable({
  providedIn: 'root'
})
export class SellerService {

  constructor(private httpClient: HttpClient) { }

  // getTotalProductCount(sellerId: number): Observable<number> {
   
  // }

  getSellerById(sellerid: number): Observable<any>{
    const params = {
      sellerid: sellerid
    }
    return this.httpClient.get<any>(baseUrl+'/getSeller', {params})
  }
}
