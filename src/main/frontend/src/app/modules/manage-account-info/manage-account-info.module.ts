import { NgModule } from '@angular/core';
import { ManageAccountInfoRoutingModule } from './manage-account-info-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../helpers/auth.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

@NgModule({
  declarations: [
    ProfileComponent,
    ChangePasswordComponent
  ],
  imports: [
	BrowserModule,
    ManageAccountInfoRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [AuthGuard]
})
export class ManageAccountInfoModule { }
