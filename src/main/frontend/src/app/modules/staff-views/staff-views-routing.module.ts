import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { StaffGuard } from 'src/app/guards/staff.guard';
import { StaffsComponent } from '../for-admin/for-admin-base/staffs/staffs.component';
import { CreateReportComponent } from '../user-views/create-report/create-report.component';
import { ReportedProductListComponent } from './reported-product-list/reported-product-list.component';
import { StaffBaseComponent } from './staff-base/staff-base.component';

const routes: Routes = [  
  {
  path: 'addreport',
  component: CreateReportComponent,
  title: "",
  canActivate: [StaffGuard]
},
{
  path: 'reportlist',
  component: ReportedProductListComponent,
  title: "",
  canActivate: [StaffGuard]
},
{
  path: 'staff',
  component: StaffBaseComponent,
  canActivate: [StaffGuard]
}];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class StaffViewsRoutingModule { 
  
}

