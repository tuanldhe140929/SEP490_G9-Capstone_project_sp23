import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

const allInspectorsUrl = "http://localhost:9000/public/manageInspector/allInspectors"
const enableUrl = "http://localhost:9000/public/manageInspector/activateInspector"
const disableUrl = "http://localhost:9000/public/manageInspector/deactivateInspector"
@Component({
  selector: 'app-all-inspectors',
  templateUrl: './all-inspectors.component.html',
  styleUrls: ['./all-inspectors.component.css']
})
export class AllInspectorsComponent implements OnInit{
  constructor(private httpClient: HttpClient, private router: Router){}

  inspectorList: any;

  ngOnInit(): void {
    this.httpClient.get<any>(allInspectorsUrl).subscribe(
      response => {
        this.inspectorList = response;
      }
    )
  }

  onEnable(id: number){ 
    this.httpClient.put<any>(enableUrl+'?inspectorId='+id,null).subscribe(
      response => {
        console.log(response)
      }
    )

    }
  
    onDisable(id: number){ 
      this.httpClient.put<any>(disableUrl+'?inspectorId='+id,null).subscribe(
        response => {
          console.log(response)
        }
      )
  
      }
    
      toAddInspector(){
        this.router.navigate(['addInspector']);
      }
}
