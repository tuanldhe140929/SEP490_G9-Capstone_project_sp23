import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';
import { Category } from '../DTOS/Category';
import { Tag } from '../DTOS/Tag';
import { Preview } from '../DTOS/Preview';
import { ProductFile } from '../DTOS/ProductFile';
const baseUrl = 'http://localhost:9000/private/manageProduct';
const serveMediaUrl = "http://localhost:9000/public/serveMedia";
const getScanVirusUrl = "https://www.virustotal.com/api/v3/files/upload_url";
const virusTotalBaseUrl = "http://www.virustotal.com/_ah/upload/";
@Injectable({
  providedIn: 'root'
})
export class ManageProductService {


  scanVirus(file:Blob) {
    let formData = new FormData();
    formData.append("file",file);
    this.getVirusTotalKey().subscribe(
      (data: string) => {
        const key = data;
        this.getUploadUrl(key).subscribe(
          (data: any) => {
            return this.httpClient.post(data.data, formData, { headers: {"x-apikey":key} }).subscribe((data:any) => {
              console.log(data);
              var analysisUrl = 'https://www.virustotal.com/api/v3/analyses/' + data.data.id;
              this.httpClient.get(analysisUrl, { headers: { "x-apikey": key } }).subscribe((data) => {
                console.log(data);
              })
            });
          })
      },
      (error: any) => {
      }
    )
  }

  getUploadUrl(key: string) {
    return this.httpClient.get(getScanVirusUrl, {
      headers: {
        "x-apikey": key
      }
    })
  }
  getTypeList(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(baseUrl + '/getTypeList');
  }
  getTagList(): Observable<Tag[]> {
    return this.httpClient.get<Tag[]>(baseUrl + '/getTagList');
  }
  getProductByIdAndSeller(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + '/getLatestVersionProductByIdAndSeller?productId=' + productId);
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
  getCurrentOwnerInfo(username: string): Observable<User> {
    return this.httpClient.get<User>(baseUrl + "/getCurrentOwnerInfo", {
      params: {
        username: username
      }
    })
  }

  getVirusTotalKey() {
    return this.httpClient.get(baseUrl + "/getVirusTotalKey", {
      responseType: 'text',
    })
  }
}
