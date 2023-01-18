import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ForgotPasswordComponent } from './login/forgot-password/forgot-password.component';
import { LoginWithGoogleComponent } from './login/login-with-google/login-with-google.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
	{
		path: 'login',
		component: LoginComponent
	},
	{
		path: 'forgotPassword',
		component: ForgotPasswordComponent
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
