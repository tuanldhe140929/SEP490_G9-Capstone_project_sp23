import { Component } from '@angular/core';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class Report {
      userId: number;
      productId: number;
      description: string;
      created_date: string;
      violation_type_id: number
}
