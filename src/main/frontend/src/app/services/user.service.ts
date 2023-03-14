import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../DTOS/User';

const baseUrl = "http://localhost:9000/user";
@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private httpClient: HttpClient) { }

  getUserById(id: number): Observable<any>{
    return this.httpClient.get<any>(`${baseUrl}/getUserById/${id}`);
  }
}
