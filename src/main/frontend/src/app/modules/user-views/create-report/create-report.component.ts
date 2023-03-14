import { Component, OnInit } from '@angular/core';
import { Report } from 'src/app/DTOS/Report';

@Component({
  selector: 'app-create-report',
  templateUrl: './create-report.component.html',
  styleUrls: ['./create-report.component.css']
})
export class CreateReportComponent implements OnInit{
  report: Report = new Report();
  constructor(){}
  ngOnInit(): void {
  }
onSubmit(){}
}
