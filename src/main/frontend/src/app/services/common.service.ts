import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../DTOS/User';

const baseUrl = 'http://localhost:9000/public/common';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor(private http: HttpClient) { }

  getUserInfoByUsername(username: string): Observable<User> {
    return this.http.get<User>(baseUrl + '/getUserInfoByUsername?username='+username);
  }

  getProductsByUsername(username: string): Observable<any> {
    return this.http.get<any>(baseUrl + '/getProductsByUsername?username=' + username);
  }

}
