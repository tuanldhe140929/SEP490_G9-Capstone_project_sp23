import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction, TransactionStatus } from '../dtos/Transaction';

const baseUrl = 'http://localhost:9000/transaction';
@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  cancelPayment(transId: number): Observable<boolean> {
    return this.httpClient.post<boolean>(baseUrl + '/cancelTransaction', null, {
      params: {
        transId: transId
      }
    });
  }
  reviewTransaction(paymentId: any, token: any, payerId: any): Observable<Transaction> {
    return this.httpClient.get<Transaction>(baseUrl + '/reviewTransaction?paymentId=' + paymentId + '&payerId=' + payerId);
  }

  constructor(private httpClient: HttpClient) { }

  public purchase(cartId: number): Observable<Transaction> {
    return this.httpClient.post<Transaction>(baseUrl + '/purchase?cartId=' + cartId, null);
  }

  public checkTransactionStatus(transactionId: number): Observable<TransactionStatus> {
    return this.httpClient.get<TransactionStatus>(baseUrl + '/checkTransactionStatus?transactionId=' + transactionId);
  }

  public executePayment(paymentId: string, payerId: string): Observable<Transaction> {
    return this.httpClient.post<Transaction>(baseUrl + '/executeTransaction',null, {
      params: {
        paymentId: paymentId,
        PayerID: payerId
      }
    })
  }
  getPurchasedProductList(userId: number){
    return this.httpClient.get<any>("http://localhost:9000/transaction/getPurchasedProductList");
  }
}
