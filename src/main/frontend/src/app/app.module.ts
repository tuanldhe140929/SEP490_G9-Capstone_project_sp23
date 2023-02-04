import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthInterceptor } from './helpers/auth.interceptor';
import { AuthModule } from './modules/auth/auth.module';
import { ManageProductModule } from './modules/manage-product/manage-product.module';
import { CommonModule } from './modules/common/common.module';
import { BasicsModule } from './modules/basics/basics.module';
import {MatIconModule} from '@angular/material/icon';	
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { ManageAccountInfoModule } from './modules/manage-account-info/manage-account-info.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ManageInspectorModule } from './modules/manage-inspector/manage-inspector.module';
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
    ManageProductModule,
    ManageAccountInfoModule,
    CommonModule,
    BasicsModule,
    MatIconModule,
    CKEditorModule,
    ManageInspectorModule,
    NgbModule
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
