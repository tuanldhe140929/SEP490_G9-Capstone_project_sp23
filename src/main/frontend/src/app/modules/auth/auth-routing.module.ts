import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ForgotPasswordComponent } from './login/forgot-password/forgot-password.component';
import { LoginWithGoogleComponent } from './login/login-with-google/login-with-google.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
	{
		path: 'login',
    component: LoginComponent,
    title:'Login'
	},
	{
		path: 'forgotPassword',
    component: ForgotPasswordComponent,
    title: 'Reset password'
	},
	{
		path:'register',
    component: RegisterComponent,
    title: 'Create new account'
	},
	{
		path:'auth/loginWithGoogle',
    component: LoginWithGoogleComponent,
    title: 'Google Login'
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
