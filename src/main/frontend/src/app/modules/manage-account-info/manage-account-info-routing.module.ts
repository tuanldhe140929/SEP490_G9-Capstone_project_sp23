import { inject, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { AuthGuard } from '../../helpers/auth.guard';
import { ProfileComponent } from './profile/profile.component';
import { ChangePasswordComponent } from './change-password/change-password.component';


const routes: Routes = [
	{
		path:'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
	},
	{
		path: 'changepassword',
		component: ChangePasswordComponent
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageAccountInfoRoutingModule { }
