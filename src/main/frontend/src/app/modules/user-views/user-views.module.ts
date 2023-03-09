import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserViewsRoutingModule } from './user-views-routing.module';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../guards/auth.guard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CheckoutComponent } from './checkout/checkout.component';
import { DownloadComponent } from './download/download.component';
import { CartComponent } from './cart/cart.component';


@NgModule({
  declarations: [
    ChangePasswordComponent,
    ProfileComponent,
    CheckoutComponent,
    DownloadComponent,
    CartComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    UserViewsRoutingModule
  ],
  providers: [AuthGuard]
})
export class UserViewsModule { }
