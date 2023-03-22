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


import { StaffViewsRoutingModule } from './staff-views-routing.module';


import { ReportedProductListComponent } from './reported-product-list/reported-product-list.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { StaffBaseComponent } from './staff-base/staff-base.component';
import { ProductApprovalComponent } from './staff-base/product-approval/product-approval.component';
import { ReportListComponent } from './staff-base/report-list/report-list.component';
import { AddviolationComponent } from './addviolation/addviolation.component';
import { UpdateApprovalComponent } from './staff-base/product-approval/update-approval/update-approval.component';
import { ApprovalProductDetailsComponent } from './staff-base/product-approval/approval-product-details/approval-product-details.component';
import { ApprovalDownloadComponent } from './staff-base/product-approval/approval-download/approval-download.component';
import { ReportedProductDetailsComponent } from './staff-base/report-list/reported-product-details/reported-product-details.component';


@NgModule({
  declarations: [
    ReportedProductListComponent,
    StaffBaseComponent,
    ProductApprovalComponent,
    ReportListComponent,
    AddviolationComponent
    UpdateApprovalComponent,
    ApprovalProductDetailsComponent,
    ApprovalDownloadComponent,
    ReportedProductDetailsComponent,
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
    MatSelectModule,
    VgControlsModule,
    VgCoreModule,
    VgBufferingModule,
    VgOverlayPlayModule
  ]
})
export class StaffViewsModule { }
