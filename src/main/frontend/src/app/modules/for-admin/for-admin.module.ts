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

import { ForAdminRoutingModule } from './for-admin-routing.module';
import { InspectorListComponent } from './inspector-list/inspector-list.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { AddInspectorComponent } from './add-inspector/add-inspector.component';
import { UpdateInspectorComponent } from './update-inspector/update-inspector.component';
import { DeleteInspectorComponent } from './delete-inspector/delete-inspector.component';

@NgModule({
  declarations: [
    InspectorListComponent,
    AdminDashboardComponent,
    ForAdminBaseComponent,
    AddInspectorComponent,
    UpdateInspectorComponent,
    DeleteInspectorComponent
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
    DataTablesModule
  ]
})
export class ForAdminModule { }