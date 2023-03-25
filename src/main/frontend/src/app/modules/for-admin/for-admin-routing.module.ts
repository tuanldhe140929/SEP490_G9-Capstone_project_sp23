import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { ReactiveFormsModule } from '@angular/forms';

import { DashboardComponent } from './for-admin-base/dashboard/dashboard.component';
import { StaffsComponent } from './for-admin-base/staffs/staffs.component';
import { AdminGuard } from 'src/app/guards/admin.guard';
import { CategoriesComponent } from './for-admin-base/categories/categories.component';
import { TagsComponent } from './for-admin-base/tags/tags.component';

const routes: Routes = [
  {
    path: 'admin',
    component: ForAdminBaseComponent,
    title: 'Hệ thống DPM dành cho Quản trị viên',
    children: [
      {
        path: '',
        outlet: 'dashboard',
        component: DashboardComponent,
        pathMatch: 'full'
      },
      {
        path: '',
        outlet: 'staffs',
        component: StaffsComponent,
        pathMatch: 'full'
      },
      {
        path: '',
        outlet: 'categories',
        component: CategoriesComponent,
        pathMatch: 'full'
      },
      {
        path: '',
        outlet: 'tags',
        component: TagsComponent,
        pathMatch: 'full'
      }
    ],
    canActivate: [AdminGuard]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForAdminRoutingModule { }
