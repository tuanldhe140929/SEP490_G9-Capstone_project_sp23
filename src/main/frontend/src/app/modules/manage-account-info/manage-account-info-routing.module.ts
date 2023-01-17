import { inject, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
	{
		path:'profile',
		component:ProfileComponent,
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageAccountInfoRoutingModule { }
