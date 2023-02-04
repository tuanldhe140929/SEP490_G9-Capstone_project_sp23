import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AuthGuard } from '../../helpers/auth.guard';
import { ManageProductRoutingModule } from './manage-product-routing.module';
import { NewProductComponent } from './new-product/new-product.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressBarModule } from '@angular/material/progress-bar'; 
import { BrowserModule } from '@angular/platform-browser';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete'
import { MatFormFieldModule } from '@angular/material/form-field';

import { CKEditorModule} from '@ckeditor/ckeditor5-angular';


    
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
    MatProgressBarModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    CKEditorModule
  ],
  providers: [AuthGuard, DecimalPipe],

})
export class ManageProductModule { }
