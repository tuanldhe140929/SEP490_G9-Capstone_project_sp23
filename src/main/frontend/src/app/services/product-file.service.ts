import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductFile } from '../DTOS/ProductFile';

const baseUrl = "http://localhost:9000/productFile"

@Injectable({
  providedIn: 'root'
})
export class ProductFileService {

  constructor(private httpClient: HttpClient) { }

  uploadProductFile(data: any): Observable<HttpEvent<any>> {
    return this.httpClient.post<HttpEvent<any>>(baseUrl + '/uploadProductFile', data, {
      reportProgress: true,
      responseType: 'json',
      observe: 'events',
    });
  }

  deleteProductFile(data: any): Observable<ProductFile> {
    return this.httpClient.post<ProductFile>(baseUrl + '/deleteProductFile', data, {

    });
  }
}
