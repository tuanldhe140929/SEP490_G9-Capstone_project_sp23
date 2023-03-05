import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';

const baseUrl = "http://localhost:9000/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) { }

  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + "/getActiveVersionProductById", {
      params: {
        productId: productId
      }
    });
  }

  public getProductsCountBySellerId(sellerId: number): Observable<number> {
    return this.httpClient.get<number>(baseUrl + "/getProductsCountBySellerId", {
      params: {
        sellerId: sellerId
      }
    });
  }
}
