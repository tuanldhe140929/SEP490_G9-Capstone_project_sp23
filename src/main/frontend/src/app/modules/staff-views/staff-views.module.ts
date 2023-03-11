import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StaffViewsRoutingModule } from './staff-views-routing.module';
import { CreateReportComponent } from './create-report/create-report.component';
import { ReportedProductListComponent } from './reported-product-list/reported-product-list.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    ReportedProductListComponent,
    CreateReportComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    StaffViewsRoutingModule
  ]
})
export class StaffViewsModule { }
