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
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { ProductCollectionComponent } from './product-collection/product-collection.component';
import { SellerGuardGuard } from '../../helpers/seller-guard.guard';

    
@NgModule({
  declarations: [

    NewProductComponent,
      ProductCollectionComponent
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
    CKEditorModule,
    VgCoreModule,
    VgControlsModule,
    VgOverlayPlayModule,
    VgBufferingModule
  ],
  providers: [SellerGuardGuard, DecimalPipe],

})
export class ManageProductModule { }
