import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';
import { Type } from '../DTOS/Type';
import { Tag } from '../DTOS/Tag';
const baseUrl = 'http://localhost:9000/private/manageProduct';

@Injectable({
  providedIn: 'root'
})
export class ManageProductService {
  getTypeList(): Observable<Type[]> {
    return this.httpClient.get<Type[]>(baseUrl + '/getTypeList');
  }
  getTagList(): Observable<Tag[]> {
    return this.httpClient.get<Tag[]>(baseUrl + '/getTagList');
  }
  getProductByIdAndUser(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + '/getProductByIdAndUser?productId=' + productId);
  }

  constructor(private httpClient: HttpClient) { }

  getCurrentUserInfo(email: string): Observable<User> {
    return this.httpClient.get<User>(baseUrl + '/getCurrentUserInfo?email=' + email);
  }

  uploadCoverImage(data: any): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/uploadCoverImage', data, {
      reportProgress: true,
    });
  }
  
  uploadProductFile(data: any): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/uploadProductFile', data, {
      //reportProgress: true,
    });
  }
  
  deleteProductFile(data: any): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/deleteProductFile', data, {
      
    });
  }
  
  getCoverImage(productId: number): Observable<Blob> {
    return this.httpClient.get(baseUrl + "/serveCoverImage?productId=" + productId, {
      responseType: 'blob'
    });
  }

  updateProduct(data: any, instructionDetail: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/updateProduct', data, {
      params: {
        instruction: instructionDetail
      }
    });
  }
}
