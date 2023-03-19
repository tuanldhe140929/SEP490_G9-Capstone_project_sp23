import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from '../DTOS/Transaction';

const baseUrl = 'http://localhost:9000/transaction';
@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private httpClient:HttpClient) { }
  
  public purchase(cartId:number):Observable<Transaction>{
	  return this.httpClient.post<Transaction>(baseUrl+'/purchase?cartId='+cartId,null);
  }
}
