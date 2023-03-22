import { Component } from '@angular/core';
import { Report } from '../../../DTOS/Report';

@Component({
  selector: 'app-reported-product-list',
  templateUrl: './reported-product-list.component.html',
  styleUrls: ['./reported-product-list.component.css']
})
export class ReportedProductListComponent {
  reports: Report[];
  constructor() { }

  // ngOnInit(): void {
  //   this.reports = [{
  //     "userId": 1,
  //     "productId": 1,
  //     "description": "Like",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 2,
  //     "productId": 2,
  //     "description": "Hate",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 3,
  //     "productId": 3,
  //     "description": "Low",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 4,
  //     "productId": 4,
  //     "description": "High Value",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 5,
  //     "productId": 5,
  //     "description": "Low Quality",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 6,
  //     "productId": 6,
  //     "description": "Low Price",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 7,
  //     "productId": 7,
  //     "description": "Like",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 8,
  //     "productId": 8,
  //     "description": "Hate",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 9,
  //     "productId": 9,
  //     "description": "Low",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 10,
  //     "productId": 10,
  //     "description": "High Value",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 11,
  //     "productId": 11,
  //     "description": "Low Quality",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   },
  //   {
  //     "userId": 12,
  //     "productId": 12,
  //     "description": "Low Price",
  //     "created_date": "2023-03-06",
  //     "violation_type_id": 1
  //   }];
  // }

}
