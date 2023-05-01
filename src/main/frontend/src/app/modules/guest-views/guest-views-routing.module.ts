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
import { SellerSearchResultComponent } from './seller-search-result/seller-search-result.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { SearchResultCategoryComponent } from './search-result-category/search-result-category.component';
import { SearchResultTagComponent } from './search-result-tag/search-result-tag.component';
import { ContactComponent } from '../common-views/contact/contact.component';

const routes: Routes = [{
  path: 'login',
  component: LoginComponent,
  title: 'Đăng nhập'
},
  {
    path: 'forgotPassword',
    component: ResetPasswordComponent,
    title: 'Đặt lại mật khẩu'
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: 'Tạo tài khoản mới'
  },
  {
	path:  'home',
	component:HomeComponent,
	title: 'Trang chủ DPM'
  },
  {
    path:  'contact',
    component:ContactComponent,
    title: 'Trang contact'
    },
  {
    path: 'auth/login-with-google',
    component: LoginWithGoogleComponent,
    title: 'Đăng nhập bằng Google'
  },
  {
    path: 'auth/verify-email/:verifyLink',
    component: VerifyEmailComponent,
    title: 'Xác thực Email'
  },
  {
    path: 'collection/:sellerId',
    component: SellerProductListComponent,
    title: "Các sản phẩm của người dùng",
  }, {
    path: 'products/:productId',
    component: ProductDetailsComponent,
    title: 'Chi tiết sản phẩm'
  }, {
    path: 'result/:keyword',
    component: SearchResultComponent,
    title: 'Kết quả tìm kiếm sản phẩm'
  }, {
    path: 'sellers/:keyword',
    component: SellerSearchResultComponent,
    title: 'Kết quả tìm kiếm người bán'
  }, {
    path: 'category/:categoryId',
    component: SearchResultCategoryComponent,
    title: 'Tìm kiếm sản phẩm'
  }, {
    path: 'tag/:tagId',
    component: SearchResultTagComponent,
    title: 'Tìm kiếm sản phẩm'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GuestViewsRoutingModule { }
