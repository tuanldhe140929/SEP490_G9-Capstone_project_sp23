import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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
}
