import { inject, NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { AuthGuard } from '../../helpers/auth.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { ProfileComponent } from './profile/profile.component';
import { ChangeNameComponent } from './change-name/change-name.component';
import { BrowserModule } from '@angular/platform-browser';

const routes: Routes = [
	{
		path:'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
	},
	{
		path: 'changepassword',
		component: ChangePasswordComponent,
    
	},
	{
		path: 'changename',
		component: ChangeNameComponent,
    canActivate: [AuthGuard]
	}
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageAccountInfoRoutingModule { }
