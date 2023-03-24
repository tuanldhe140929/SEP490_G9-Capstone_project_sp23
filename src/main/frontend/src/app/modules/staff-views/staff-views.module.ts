import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { DataTablesModule } from 'angular-datatables';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatSortModule } from '@angular/material/sort';
import { MatMenuModule } from '@angular/material/menu';


import { StaffViewsRoutingModule } from './staff-views-routing.module';


import { ReportedProductListComponent } from './reported-product-list/reported-product-list.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { StaffBaseComponent } from './staff-base/staff-base.component';
import { ProductApprovalComponent } from './staff-base/product-approval/product-approval.component';
import { ReportListComponent } from './staff-base/report-list/report-list.component';
import { AddviolationComponent } from './staff-base/addviolation/addviolation.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    ReportedProductListComponent,
    StaffBaseComponent,
    ProductApprovalComponent,
    ReportListComponent,
    AddviolationComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    StaffViewsRoutingModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatTableModule,
    MatDialogModule,
    MatButtonModule,
    DataTablesModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatInputModule,
    MatSortModule,
    MatMenuModule,
    ReactiveFormsModule
  ]
})
export class StaffViewsModule { }
