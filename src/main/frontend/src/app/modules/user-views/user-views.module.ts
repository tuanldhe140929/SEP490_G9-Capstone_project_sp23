import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonViewsModule } from '../common-views/common-views.module';

import { UserViewsRoutingModule } from './user-views-routing.module';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../guards/auth.guard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DownloadComponent } from './download/download.component';
import { CartComponent } from './cart/cart.component';
import { ReviewTransactionComponent } from './review-transaction/review-transaction.component';
import { LoadingSpinnerComponent } from '../common-views/loading-spinner/loading-spinner.component';
import { LoginWithPaypalComponent } from './login-with-paypal/login-with-paypal.component';
import { PurchasedProductListComponent } from './purchased-product-list/purchased-product-list.component';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [
    ChangePasswordComponent,
    ProfileComponent,
    DownloadComponent,
    CartComponent,
    ReviewTransactionComponent,
    LoginWithPaypalComponent,
    PurchasedProductListComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    UserViewsRoutingModule,
    CommonViewsModule,
    NgxPaginationModule
  ],
  providers: [AuthGuard]
})
export class UserViewsModule { }
