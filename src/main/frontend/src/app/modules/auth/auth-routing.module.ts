import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginWithGoogleComponent } from './login/login-with-google/login-with-google.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
	{
		path: 'login',
		component: LoginComponent
	},
	{
		path:'register',
		component: RegisterComponent
	},
	{
		path:'auth/loginWithGoogle',
		component: LoginWithGoogleComponent
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
