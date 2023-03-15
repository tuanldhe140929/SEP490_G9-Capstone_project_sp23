import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Report } from 'src/app/DTOS/Report';
import { AuthGuard } from '../../guards/auth.guard';
import { CheckOutComponent } from '../for-users/check-out/check-out.component';
import { ReportedProductListComponent } from '../staff-views/reported-product-list/reported-product-list.component';
import { CartComponent } from './cart/cart.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { CreateReportComponent } from './create-report/create-report.component';

import { DownloadComponent } from './download/download.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'changepassword',
    component: ChangePasswordComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'cart',
    component: CartComponent
  },
  {
    path: 'checkout',
    component: CheckOutComponent
  },
  {

    path: 'report',
    component: CreateReportComponent
  },
  {

    path: 'download/:productId',
    component: DownloadComponent,
    title: 'Download',
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserViewsRoutingModule { }
