import { NgModule } from '@angular/core';
import { ManageAccountInfoRoutingModule } from './manage-account-info-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../helpers/auth.guard';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangePasswordComponent } from './change-password/change-password.component';


@NgModule({
  declarations: [
    ProfileComponent,
    ChangePasswordComponent
  ],
  imports: [
	BrowserModule,
	ManageAccountInfoRoutingModule,
    ReactiveFormsModule,
    CommonModule,
    FormsModule
    
  ],
  providers: [AuthGuard]
})
export class ManageAccountInfoModule { }
