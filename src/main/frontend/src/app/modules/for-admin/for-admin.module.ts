import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';

import { ForAdminRoutingModule } from './for-admin-routing.module';
import { InspectorListComponent } from './inspector-list/inspector-list.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { AddInspectorComponent } from './add-inspector/add-inspector.component';

@NgModule({
  declarations: [
    InspectorListComponent,
    AdminDashboardComponent,
    ForAdminBaseComponent,
    AddInspectorComponent
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
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ForAdminModule { }
