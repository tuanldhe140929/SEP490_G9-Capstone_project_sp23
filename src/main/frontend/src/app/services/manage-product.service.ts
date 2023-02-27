import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';
import { Category } from '../DTOS/Category';
import { Tag } from '../DTOS/Tag';
import { Preview } from '../DTOS/Preview';
import { ProductFile } from '../DTOS/ProductFile';
const baseUrl = 'http://localhost:9000/product';
const serveMediaUrl = "http://localhost:9000/public/serveMedia";

@Injectable({
  providedIn: 'root'
})
export class ManageProductService {
  getProductByIdAndVersionAndSeller(productId: number, version: string): Observable<Product> {
    return this.httpClient.post<Product>(baseUrl + '/chooseVersion', null, {
      params: {
        productId: productId,
        version: version
      }
    });
  }

  getAllVersionOfProduct(productId: number): Observable<string[]> {
    return this.httpClient.get<string[]>(baseUrl + '/getAllVersion', {
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

  getProductByIdAndSeller(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(baseUrl + '/getProductById?productId=' + productId);
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
    return this.httpClient.post<Product>(baseUrl + '/updateProduct', data, {
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
