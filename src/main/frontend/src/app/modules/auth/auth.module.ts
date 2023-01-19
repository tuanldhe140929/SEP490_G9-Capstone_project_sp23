import { NgModule } from '@angular/core';
import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { LoginWithGoogleComponent } from './login/login-with-google/login-with-google.component';
import { RegisterComponent } from './register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ForgotPasswordComponent } from './login/forgot-password/forgot-password.component';


@NgModule({
  declarations: [
    RegisterComponent,
    LoginComponent,
    LoginWithGoogleComponent,
    ForgotPasswordComponent
  ],
  imports: [
    AuthRoutingModule,
    ReactiveFormsModule,
  ]
})
export class AuthModule { }
