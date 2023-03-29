import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ViolationService {

  baseUrl = "http://localhost:9000/violation/";

  constructor(private httpClient: HttpClient) { }

  getAllTypes(): Observable<any>{
    return this.httpClient.get<any>(this.baseUrl+"getAllVioTypes");
  }
  
}
