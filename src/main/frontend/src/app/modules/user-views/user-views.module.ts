import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonViewsModule } from '../common-views/common-views.module';

import { UserViewsRoutingModule } from './user-views-routing.module';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../guards/auth.guard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CheckoutComponent } from './checkout/checkout.component';
import { DownloadComponent } from './download/download.component';
import { CartComponent } from './cart/cart.component';
import { ThankYouComponent } from './thank-you/thank-you.component';


@NgModule({
  declarations: [
    ChangePasswordComponent,
    ProfileComponent,
    CheckoutComponent,
    DownloadComponent,
    CartComponent,
    ThankYouComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    UserViewsRoutingModule,
    CommonViewsModule
  ],
  providers: [AuthGuard]
})
export class UserViewsModule { }
