import { inject, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from 'src/app/helpers/AuthGuardService';
import { AuthService } from 'src/app/services/auth.service';
import { ProfileComponent } from './profile/profile.component';


const routes: Routes = [
	{
		path:'profile',
		component:ProfileComponent,
		canActivate: [AuthGuardService] 
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageAccountInfoRoutingModule { }
