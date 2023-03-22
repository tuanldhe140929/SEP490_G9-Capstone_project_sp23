import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-addviolation',
  templateUrl: './addviolation.component.html',
  styleUrls: ['./addviolation.component.css']
})
export class AddviolationComponent {

getViolation = "http://localhost:9000/addviolation";

  constructor(private httpClient: HttpClient) { }

  getAllTypes(): Observable<any>{
    return this.httpClient.get<any>(this.getViolation);
  }
  
}
