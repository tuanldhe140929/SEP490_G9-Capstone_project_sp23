import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


const baseUrl = "http://localhost:9000/product";

@Injectable({
  providedIn: 'root'
})



export class ProductDtoService {

  constructor(private httpClient: HttpClient) { }

  getAllProducts(): Observable<any>{
    return this.httpClient.get<any>(baseUrl+'/getAllProducts');
  }

  getProductsByKeyword(keyword: string): Observable<any>{
    return this.httpClient.get<any>(baseUrl+"/getProductsByKeyword/"+keyword);
  }
}
