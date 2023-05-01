import { HttpClient, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductFile } from '../dtos/ProductFile';

const baseUrl = "http://localhost:9000/productFile"

@Injectable({
  providedIn: 'root'
})
export class ProductFileService {

  constructor(private httpClient: HttpClient) { }

  downloadForStaff(pid: number, version: string) {
    return this.httpClient.get(baseUrl + "/downloadForStaff", {
      params: {
        productId: pid,
        version: version
      },
      headers: {
        "Content-Type": 'application/zip'
      },
      responseType: "blob"
    })
  }
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

  generateDownloadToken(userId: number, productId: number): Observable<string> {
    return this.httpClient.post(baseUrl + "/generateDownloadToken/" + productId, null, {
      responseType: 'text'
    });
  }


  download(productId: number, token: string) {
    return this.httpClient.get(baseUrl + "/download", {
      params: {
        productId:productId,
        token: token
      },
      headers: {
        "Content-Type": 'application/zip'
      },
      responseType: "blob"
    })
  }
}
