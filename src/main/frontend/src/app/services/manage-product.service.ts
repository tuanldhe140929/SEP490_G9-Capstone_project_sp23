import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';
import { Type } from '../DTOS/Type';
import { Tag } from '../DTOS/Tag';
import { Preview } from '../DTOS/Preview';
const baseUrl = 'http://localhost:9000/private/manageProduct';
const serveMediaUrl = "http://localhost:9000/public/serveMedia";
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

  uploadCoverImage(data: any): any {
    return this.httpClient.post(baseUrl + '/uploadCoverImage', data, {
      responseType: 'text',
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
    return this.httpClient.get(serveMediaUrl + "/serveCoverImage?productId=" + productId, {
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

  getPreviewVideo(previewId: number): any {
    return this.httpClient.get(serveMediaUrl + "/servePreviewVideo/" + previewId, {
      headers: { 'Range': 'bytes=0-500' }
    });
  }

  uploadPreviewVideo(data: any): Observable<Preview> {
    return this.httpClient.post<Preview>(baseUrl + '/uploadPreviewVideo', data, {
      reportProgress: true
    });
  }

  uploadPreviewPicture(data: any): Observable<Preview[]> {
    return this.httpClient.post<Preview[]>(baseUrl + '/uploadPreviewPicture', data);
  }

  removePreviewVideo(productId: number) {
    return this.httpClient.delete(baseUrl + "/removePreviewVideo", {
      params: {
        productId: productId
      }
    });
  }

  removePreviewPicture(previewId: number): Observable<Preview[]> {
    return this.httpClient.delete<Preview[]>(baseUrl + "/removePreviewPicture", {
      params: {
        previewId: previewId
      }
    })
  }
  getCurrentOwnerInfo(username: string): Observable<User>{
    return this.httpClient.get<User>(baseUrl+"/getCurrentOwnerInfo",{
      params:{
        username: username
      }
    })
  }
}