import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginWithGoogleComponent } from './login-with-google/login-with-google.component';
import { LoginComponent } from './login/login.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SearchResultComponent } from './search-result/search-result.component';
import { SellerProductListComponent } from './seller-product-list/seller-product-list.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';

const routes: Routes = [{
  path: 'login',
  component: LoginComponent,
  title: 'Login'
},
  {
    path: 'forgotPassword',
    component: ResetPasswordComponent,
    title: 'Reset password'
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: 'Create new account'
  },
  {
	path:  'homepage',
	component:HomeComponent,
	title: 'Homepage'
  },
  {
    path: 'auth/login-with-google',
    component: LoginWithGoogleComponent,
    title: 'Google Login'
  },
  {
    path: 'auth/verify-email/:verifyLink',
    component: VerifyEmailComponent,
    title: 'Verify Email'
  },
  {
    path: 'collection/:sellerId',
    component: SellerProductListComponent,
    title: "User product collection",
  }, {
    path: 'products/:productId',
    component: ProductDetailsComponent,
    title: 'Chi tiết sản phẩm'
  }, {
    path: 'result/:keyword',
    component: SearchResultComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GuestViewsRoutingModule { }
