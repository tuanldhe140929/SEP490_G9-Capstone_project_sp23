import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';

const baseUrl = "http://localhost:9000/productDetails";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) { }

  public getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + "/getActiveVersion", {
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
  public GetProductDetails(sellerId: number): Observable<Product[]>{
    return this.httpClient.get<Product[]>(baseUrl+"/getProductDetails",{
      params:{
        sellerId: sellerId
      }
    })
  }

  getAllProducts(): Observable<any>{
    return this.httpClient.get<any>(baseUrl+'/getAllProducts');
  }

  getProductsByKeyword(keyword: string): Observable<any>{
    return this.httpClient.get<any>(baseUrl+"/getProductsByKeyword/"+keyword);
  }

  getFilteredProducts(keyword: string, categoryid: number, min: number, max: number): Observable<any>{
    const params = {
      keyword: keyword,
      categoryid: categoryid,
      min: min,
      max: max
    }
    return this.httpClient.get<any>("http://localhost:9000/productDetails/getFilteredProducts", {params});
  }
}
