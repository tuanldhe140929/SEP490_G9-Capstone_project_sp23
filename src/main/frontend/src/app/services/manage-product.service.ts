import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';
import { Category } from '../DTOS/Category';
import { Tag } from '../DTOS/Tag';
import { Preview } from '../DTOS/Preview';
import { ProductFile } from '../DTOS/ProductFile';
import { License } from '../DTOS/License';
const baseUrl = 'http://localhost:9000/product';
const serveMediaUrl = "http://localhost:9000/public/serveMedia";
const licenseBaseUrl = "http://localhost:9000/license";

@Injectable({
  providedIn: 'root'
})
export class ManageProductService {
  getProductById(): Observable<Product>{
    return this.httpClient.get<Product>("http://localhost:9000/product/getProductById")
  }
  activeVersion(version: Product): Observable<boolean> {
    return this.httpClient.post<boolean>(baseUrl + '/activeVersion', null, {
      params: {
        productId: version.id,
        version: version.version
      }
    }
    );
  }
  getProductByIdAndVersionAndSeller(productId: number, version: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/chooseVersion', null, {
      params: {
        productId: productId,
        version: version
      }
    });
  }

  getAllVersionOfProduct(productId: number): Observable<Product[]> {
    return this.httpClient.get<Product[]>(baseUrl + '/getAllVersion', {
      params: {
        productId: productId
      }
    })
  }

  constructor(private httpClient: HttpClient) { }

  createNewVersion(product: Product, newVersion: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/createNewVersionV2', product, {
      params: {
        newVersion: newVersion
      }
    }
    )
  }

  getActiveVersionProductByIdAndSeller(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + '/getActiveVersionProductById?productId=' + productId);
   }
  /*  getCurrentUserInfo(email: string): Observable<User> {
      return this.httpClient.get<User>(baseUrl + '/getCurrentUserInfo?email=' + email);
    }*/

  uploadCoverImage(data: any): any {
    return this.httpClient.post(baseUrl + '/uploadCoverImage', data, {
      responseType: 'text',
    });
  }

  getCoverImage(productId: number): Observable<Blob> {
    return this.httpClient.get(serveMediaUrl + "/serveCoverImage?productId=" + productId, {
      responseType: 'blob'
    });
  }

  updateProduct(data: any, instructionDetail: string): Observable<Product> {
    console.log(data);
    return this.httpClient.post<Product>(baseUrl + '/updateProduct', data, {
      params: {
        instruction: instructionDetail
      }
    });
  }

  getLicense(data: any, instructionDetail: string): Observable<License[]> {
    return this.httpClient.post<License[]>(baseUrl + '/updateProduct', data, {
      params: {
        instruction: instructionDetail
      }
    });
  }

  /*  getPreviewVideo(previewId: number): any {
      return this.httpClient.get(serveMediaUrl + "/servePreviewVideo/" + previewId, {
        headers: { 'Range': 'bytes=0-500' }
      });
    }
  */



  /*  getCurrentOwnerInfo(username: string): Observable<User> {
      return this.httpClient.get<User>(baseUrl + "/getCurrentOwnerInfo", {
        params: {
          username: username
        }
      })
    }*/

}
