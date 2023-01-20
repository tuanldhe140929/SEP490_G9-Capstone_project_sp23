import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const baseUrl = 'http://localhost:9000/private/productManage';

@Injectable({
  providedIn: 'root'
})
export class ManageProductService {

  constructor(private httpClient: HttpClient) { }
  
  getCurrentUserInfo(email:string){
	  return this.httpClient.get(baseUrl + '/getCurrentUserInfo?email='+email);
  }
}
