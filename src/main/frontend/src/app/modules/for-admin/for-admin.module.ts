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
import { MatSortModule } from '@angular/material/sort';

import { ForAdminRoutingModule } from './for-admin-routing.module';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { DashboardComponent } from './for-admin-base/dashboard/dashboard.component';
import { StaffsComponent } from './for-admin-base/staffs/staffs.component';
import { AddStaffComponent } from './for-admin-base/staffs/add-staff/add-staff.component';
import { UpdateStaffStatusComponent } from './for-admin-base/staffs/update-staff-status/update-staff-status.component';
import { CategoriesComponent } from './for-admin-base/categories/categories.component';
import { AddCategoryComponent } from './for-admin-base/categories/add-category/add-category.component';
import { TagsComponent } from './for-admin-base/tags/tags.component';
import { UpdateCategoryComponent } from './for-admin-base/categories/update-category/update-category.component';


@NgModule({
  declarations: [
    ForAdminBaseComponent,
    DashboardComponent,
    StaffsComponent,
    AddStaffComponent,
    UpdateStaffStatusComponent,
    CategoriesComponent,
    AddCategoryComponent,
    TagsComponent,
    UpdateCategoryComponent,
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
    MatInputModule,
    MatSortModule
  ]
})
export class ForAdminModule { }
