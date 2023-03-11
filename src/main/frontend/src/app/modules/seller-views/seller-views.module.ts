import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SellerViewsRoutingModule } from './seller-views-routing.module';
import { UpdateProductComponent } from './update-product/update-product.component';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { SellerGuardGuard } from '../../guards/seller-guard.guard';
import { DecimalPipe } from '@angular/common';


@NgModule({
  declarations: [
    UpdateProductComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    SellerViewsRoutingModule,
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
  providers: [SellerGuardGuard, DecimalPipe]
})
export class SellerViewsModule { }
