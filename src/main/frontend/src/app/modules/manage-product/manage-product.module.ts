import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ManageProductRoutingModule } from './manage-product-routing.module';
import { ProductCollectionComponent } from './product-collection/product-collection.component';


@NgModule({
  declarations: [
    ProductCollectionComponent
  ],
  imports: [
    CommonModule,
    ManageProductRoutingModule
  ]
})
export class ManageProductModule { }
