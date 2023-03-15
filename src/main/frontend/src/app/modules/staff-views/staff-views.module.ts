import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { StaffViewsRoutingModule } from './staff-views-routing.module';


import { ReportedProductListComponent } from './reported-product-list/reported-product-list.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { StaffBaseComponent } from './staff-base/staff-base.component';
import { ProductApprovalComponent } from './staff-base/product-approval/product-approval.component';


@NgModule({
  declarations: [
    ReportedProductListComponent,
    StaffBaseComponent,
    ProductApprovalComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    StaffViewsRoutingModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule
  ]
})
export class StaffViewsModule { }
