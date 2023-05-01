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
import { MatSelectModule } from '@angular/material/select';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { MatCheckboxModule } from '@angular/material/checkbox';


import { StaffViewsRoutingModule } from './staff-views-routing.module';

import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { StaffBaseComponent } from './staff-base/staff-base.component';
import { ProductApprovalComponent } from './staff-base/product-approval/product-approval.component';
import { ReportListComponent } from './staff-base/report-list/report-list.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UpdateApprovalComponent } from './staff-base/product-approval/update-approval/update-approval.component';
import { ApprovalProductDetailsComponent } from './staff-base/product-approval/approval-product-details/approval-product-details.component';
import { ApprovalDownloadComponent } from './staff-base/product-approval/approval-download/approval-download.component';
import { ReportedProductDetailsComponent } from './staff-base/report-list/reported-product-details/reported-product-details.component';
import { ReportedProductDownloadComponent } from './staff-base/report-list/reported-product-download/reported-product-download.component';
import { UpdateReportStatusComponent } from './staff-base/report-list/update-report-status/update-report-status.component';
import { ReportDescriptionComponent } from './staff-base/report-list/report-description/report-description.component';
import { ProductApprovalErrorComponent } from './staff-base/product-approval/product-approval-error/product-approval-error.component';
import { ReportErrorComponent } from './staff-base/report-list/report-error/report-error.component';



@NgModule({
  declarations: [
    StaffBaseComponent,
    ProductApprovalComponent,
    ReportListComponent,
    UpdateApprovalComponent,
    ApprovalProductDetailsComponent,
    ApprovalDownloadComponent,
    ReportedProductDetailsComponent,
    ReportedProductDownloadComponent,
    UpdateReportStatusComponent,
    ReportDescriptionComponent,
    ProductApprovalErrorComponent,
    ReportErrorComponent,
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
    ReactiveFormsModule,
    MatSelectModule,
    VgControlsModule,
    VgCoreModule,
    VgBufferingModule,
    VgOverlayPlayModule,
    MatCheckboxModule
  ]
})
export class StaffViewsModule { }
