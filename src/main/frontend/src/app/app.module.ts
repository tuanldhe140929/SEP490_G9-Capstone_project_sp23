import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatRadioModule } from '@angular/material/radio';
import { MatMenuModule } from '@angular/material/menu';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ForAdminModule } from './modules/for-admin/for-admin.module';
import { ToastrModule } from 'ngx-toastr';
import { timeout } from 'rxjs';
import { MatSortModule } from '@angular/material/sort';
import { DataTablesModule } from 'angular-datatables';
import { StaffViewsModule } from './modules/staff-views/staff-views.module';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { UserViewsModule } from './modules/user-views/user-views.module';
import { SellerViewsModule } from './modules/seller-views/seller-views.module';
import { CommonViewsModule } from './modules/common-views/common-views.module';
import { GuestViewsModule } from './modules/guest-views/guest-views.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { AddviolationComponent } from './modules/staff-views/staff-base/addviolation/addviolation.component';


@NgModule({
  declarations: [
    AppComponent,
    AddviolationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ForAdminModule,
    MatIconModule,
    MatSortModule,
    MatButtonModule,
    MatDialogModule,
    MatRadioModule,
    CKEditorModule,
    NgbModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatSortModule,
    DataTablesModule,
    ToastrModule.forRoot(
      {
        timeOut: 2000,
        progressBar: true
      }
    ),
    StaffViewsModule,
    UserViewsModule,
    SellerViewsModule,
    CommonViewsModule,
    GuestViewsModule,
    NgxPaginationModule
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
