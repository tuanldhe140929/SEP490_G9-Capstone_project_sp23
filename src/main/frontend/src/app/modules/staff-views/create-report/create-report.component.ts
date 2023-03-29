import { Component } from '@angular/core';
import { Report } from '../../../dtos/Report';

@Component({
  selector: 'app-create-report',
  templateUrl: './create-report.component.html',
  styleUrls: ['./create-report.component.css']
})
export class CreateReportComponent {
  report: Report = new Report();
  constructor() { }
  ngOnInit(): void {
  }
  onSubmit() { }
}

