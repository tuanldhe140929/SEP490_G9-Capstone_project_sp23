import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ForAdminBaseComponent } from './for-admin-base/for-admin-base.component';
import { InspectorListComponent } from './inspector-list/inspector-list.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from 'src/app/helpers/auth.guard';

const routes: Routes = [
  {
    path: 'forAdmin',
    component: ForAdminBaseComponent
  },
  {
    path: 'adminDashboard',
    component: AdminDashboardComponent
  },
  {
    path: 'inspectorList',
    component: InspectorListComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForAdminRoutingModule { }
