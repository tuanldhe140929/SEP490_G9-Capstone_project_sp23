import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ViolationTypeService {

  getAllTypesUrl = "http://localhost:9000/violation/allTypes";

  constructor(private httpClient: HttpClient) { }

  getAllTypes(): Observable<any>{
    return this.httpClient.get<any>(this.getAllTypesUrl);
  }
}
