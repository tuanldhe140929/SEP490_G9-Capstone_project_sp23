import { inject, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { AuthGuard } from '../../helpers/auth.guard';
import { ProfileComponent } from './profile/profile.component';


const routes: Routes = [
	{
		path:'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageAccountInfoRoutingModule { }
