import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';

const baseUrl = 'http://localhost:9000/seller/';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
  getSellerTotalProductCount(id: number): Observable<number> {
    return this.http.get<number>(baseUrl + '/getTotalNumberProduct');
  }
  constructor(private http: HttpClient) { }

  getCurrentLogedInUser(): Observable<User> {
    return this.http.get<User>(baseUrl + '/getCurrentLogedInUser');
  }


  getProductByIdAndUserId(productId: number, ownerId:number ): Observable<Product> {
    return this.http.get<Product>(baseUrl + '/getProductByIdAndUserId', {
      params: {
        productId: productId,
        userId: ownerId
      }
    });
  }

  getUserInfoByUsername(username: string): Observable<User> {
    return this.http.get<User>(baseUrl + '/getUserInfoByUsername?username=' + username);
  }

  getProductsByUsername(username: string): Observable<any> {
    return this.http.get<any>(baseUrl + '/getProductsByUsername?username=' + username);
  }

}
