import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgbRating } from '@ng-bootstrap/ng-bootstrap';

import { CommonViewsModule } from '../common-views/common-views.module';


import { GuestViewsRoutingModule } from './guest-views-routing.module';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { LoginWithGoogleComponent } from './login-with-google/login-with-google.component';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SellerProductListComponent } from './seller-product-list/seller-product-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { BrowserModule } from '@angular/platform-browser';
import { ReportProductComponent } from './product-details/report-product/report-product.component';
import { MatIconModule } from '@angular/material/icon';

import { HomeComponent } from './home/home.component';
import { SearchResultComponent } from './search-result/search-result.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { SellerSearchResultComponent } from './seller-search-result/seller-search-result.component';
import { DeleteProductComponent } from './seller-product-list/delete-product/delete-product.component';
import { SearchResultCategoryComponent } from './search-result-category/search-result-category.component';
import { SearchResultTagComponent } from './search-result-tag/search-result-tag.component';


@NgModule({
  declarations: [
    RegisterComponent,
    LoginComponent,
    LoginWithGoogleComponent,
    ResetPasswordComponent,
    VerifyEmailComponent,
    SellerProductListComponent,
    ProductDetailsComponent,
    ReportProductComponent,
    HomeComponent,
    SearchResultComponent,
    SellerSearchResultComponent,
    DeleteProductComponent,
    SearchResultCategoryComponent,
    SearchResultTagComponent
      
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    VgCoreModule,
    VgControlsModule,
    VgOverlayPlayModule,
    VgBufferingModule,
    MatDialogModule,
    MatSelectModule,
    MatRadioModule,
    MatAutocompleteModule,
    GuestViewsRoutingModule,
    NgbRating,
    CommonViewsModule,
    NgxPaginationModule,
    MatIconModule,
    CKEditorModule
  ]
})
export class GuestViewsModule { }
