import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../DTOS/Product';
import { User } from '../DTOS/User';

const baseUrl = 'http://localhost:9000/private/manageProduct';

@Injectable({
  providedIn: 'root'
})
export class ManageProductService {

  constructor(private httpClient: HttpClient) { }

  getCurrentUserInfo(email: string): Observable<User>{
    return this.httpClient.get<User>(baseUrl + '/getCurrentUserInfo?email=' + email);
  }
  
  uploadCoverImage(data:any):Observable<Product> {
	  return this.httpClient.post<Product>(baseUrl + '/uploadCoverImage',data,{
		   reportProgress: true,	
	  });
  }
   getCoverImage(productId:number):Observable<Blob>{
	  return this.httpClient.get(baseUrl+"/serveCoverImage?productId="+productId,{
		  responseType:'blob'
	  });
  }
}
