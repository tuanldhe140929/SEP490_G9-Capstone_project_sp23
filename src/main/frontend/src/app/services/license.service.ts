import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { License } from '../DTOS/License';


const baseUrl = "http://localhost:9000/license"
@Injectable({
  providedIn: 'root'
})
export class LicenseService {

  constructor(private httpClient:HttpClient) { }
  
  public getAllLicense():Observable<License[]>{
	  return this.httpClient.get<License[]>(baseUrl+'/getLicense');
  }
}
