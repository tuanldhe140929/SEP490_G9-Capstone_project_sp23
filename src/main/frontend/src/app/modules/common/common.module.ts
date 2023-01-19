import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CommonRoutingModule } from './common-routing.module';
import { ProductCollectionComponent } from './product-collection/product-collection.component';


@NgModule({
  declarations: [
    ProductCollectionComponent
  ],
  imports: [
    BrowserModule,
    CommonRoutingModule
  ]
})
export class CommonModule { }
