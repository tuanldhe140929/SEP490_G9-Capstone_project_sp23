import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-report-description',
  templateUrl: './report-description.component.html',
  styleUrls: ['./report-description.component.css']
})
export class ReportDescriptionComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {username: string, violationTypeName: string, description: string, reportedDate: string}, 
  ){
    
  }
}
