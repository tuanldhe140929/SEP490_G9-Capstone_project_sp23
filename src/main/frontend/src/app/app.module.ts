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
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { ManageAccountInfoModule } from './modules/manage-account-info/manage-account-info.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ForUsersModule } from './modules/for-users/for-users.module';
import { ForAdminModule } from './modules/for-admin/for-admin.module';
import { ToastrModule } from 'ngx-toastr';
import { timeout } from 'rxjs';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { Ng2OrderModule } from 'ng2-order-pipe';
import { NgxPaginationModule } from 'ngx-pagination';

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
    ForUsersModule,
    ForAdminModule,
    BasicsModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    CKEditorModule,
    NgbModule,
    BrowserAnimationsModule,
    CommonModule,
    Ng2OrderModule,
    Ng2SearchPipeModule,
    NgxPaginationModule,
    ToastrModule.forRoot(
      {
        timeOut: 2000,
        progressBar: true
      }
    ),
    
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
