import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { InspectorListComponent } from './inspector-list/inspector-list.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from 'src/app/helpers/auth.guard';
import { DashboardComponent } from './for-admin-base/dashboard/dashboard.component';
import { StaffsComponent } from './for-admin-base/staffs/staffs.component';

const routes: Routes = [
  {
    path: 'admin',
    component: ForAdminBaseComponent,
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
      }
    ]
  },
  {
    path: 'adminDashboard',
    component: AdminDashboardComponent
  },
  {
    path: 'inspectorList',
    component: InspectorListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForAdminRoutingModule { }
