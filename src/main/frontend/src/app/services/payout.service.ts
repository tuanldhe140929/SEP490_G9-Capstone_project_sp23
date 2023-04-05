import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const baseUrl = "http://localhost:9000/payout";
@Injectable({
  providedIn: 'root'
})
export class PayoutService {

  constructor(private httpClient: HttpClient) { }
  
  getPayoutHistory(){
    return this.httpClient.get<any>("http://localhost:9000/payout/getPayoutHistory");
  }
}
