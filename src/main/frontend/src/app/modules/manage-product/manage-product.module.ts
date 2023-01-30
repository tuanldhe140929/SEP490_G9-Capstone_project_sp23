import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AuthGuard } from '../../helpers/auth.guard';
import { ManageProductRoutingModule } from './manage-product-routing.module';
import { NewProductComponent } from './new-product/new-product.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressBarModule } from '@angular/material/progress-bar'; 
import { BrowserModule } from '@angular/platform-browser';
    
@NgModule({
  declarations: [

    NewProductComponent
  ],
  imports: [
	BrowserModule,
	BrowserAnimationsModule,
    ManageProductRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatProgressBarModule
    
  ],
  providers: [AuthGuard]
})
export class ManageProductModule { }
