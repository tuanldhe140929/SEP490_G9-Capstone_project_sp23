import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';

import { AuthGuard } from '../../guards/auth.guard';
import { CheckOutComponent } from '../for-users/check-out/check-out.component';
import { CartComponent } from './cart/cart.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { CreateReportComponent } from './create-report/create-report.component';

import { DownloadComponent } from './download/download.component';
import { LoginWithPaypalComponent } from './login-with-paypal/login-with-paypal.component';
import { ProfileComponent } from './profile/profile.component';
import { ReviewTransactionComponent } from './review-transaction/review-transaction.component';
import { PurchasedProductListComponent } from './purchased-product-list/purchased-product-list.component';
import { Title } from '@angular/platform-browser';

const routes: Routes = [
  {
    path: 'profile',
    component: ProfileComponent,
    title:'Trang cá nhân',
    canActivate: [AuthGuard]
  },
  {
    path: 'changepassword',
    component: ChangePasswordComponent,
    title:'Đổi mật khẩu',
    canActivate: [AuthGuard]
  },
  {
    path: 'cart',
    component: CartComponent,
    title: 'Giỏ hàng',
    canActivate: [AuthGuard]
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
  },

  {
    path: 'transaction/reviewTransaction',
    component: ReviewTransactionComponent,
    title: 'Xác nhận đơn hàng',
    canActivate: [AuthGuard]
  },
  {
  path: 'seller/createNewSeller',
    component: LoginWithPaypalComponent,
    title: 'Trở thành người bán',
    canActivate: [AuthGuard]
  },
{
  path: 'purchased',
  component: PurchasedProductListComponent,
  title: 'PurchasedProductList',
  canActivate: [AuthGuard]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserViewsRoutingModule { }
