import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';

import { ForAdminRoutingModule } from './for-admin-routing.module';
import { InspectorListComponent } from './inspector-list/inspector-list.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { AddInspectorComponent } from './add-inspector/add-inspector.component';
import { UpdateInspectorComponent } from './update-inspector/update-inspector.component';
import { DeleteInspectorComponent } from './delete-inspector/delete-inspector.component';
import { DashboardComponent } from './for-admin-base/dashboard/dashboard.component';
import { StaffsComponent } from './for-admin-base/staffs/staffs.component';
import { AddStaffComponent } from './for-admin-base/staffs/add-staff/add-staff.component';
import { UpdateStaffStatusComponent } from './for-admin-base/staffs/update-staff-status/update-staff-status.component';

@NgModule({
  declarations: [
    InspectorListComponent,
    AdminDashboardComponent,
    ForAdminBaseComponent,
    AddInspectorComponent,
    UpdateInspectorComponent,
    DeleteInspectorComponent,
    DashboardComponent,
    StaffsComponent,
    AddStaffComponent,
    UpdateStaffStatusComponent
  ],
  imports: [
    CommonModule,
    ForAdminRoutingModule,
    MatIconModule,
    MatSidenavModule,
    MatToolbarModule,
    MatTableModule,
    MatDialogModule,
    MatButtonModule,
    MatListModule,
    FormsModule,
    ReactiveFormsModule,
    DataTablesModule,
    MatFormFieldModule,
    MatPaginatorModule,
    MatInputModule
  ]
})
export class ForAdminModule { }
