import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './helpers/auth.interceptor';
import { AuthModule } from './modules/auth/auth.module';
import { ManageAccountInfoModule } from './modules/manage-account-info/manage-account-info.module';
import { ManageProductModule } from './modules/manage-product/manage-product.module';
import { CommonModule } from './modules/common/common.module';
import { BasicsModule } from './modules/basics/basics.module';
import {MatIconModule} from '@angular/material/icon';	

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AuthModule,
    ManageAccountInfoModule,
    ManageProductModule,
    CommonModule,
    BasicsModule,
    MatIconModule
    
  
  ],
  providers: [
	      {  
      provide: HTTP_INTERCEPTORS,  
      useClass: AuthInterceptor,  
      multi: true  
    }  
  ],  
  bootstrap: [AppComponent]
})
export class AppModule { }
