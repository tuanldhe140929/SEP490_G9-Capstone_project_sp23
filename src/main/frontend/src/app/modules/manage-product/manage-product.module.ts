import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from '../../helpers/auth.guard';
import { ManageProductRoutingModule } from './manage-product-routing.module';
import { NewProductComponent } from './new-product/new-product.component';
@NgModule({
  declarations: [

    NewProductComponent
  ],
  imports: [
    ManageProductRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [AuthGuard]
})
export class ManageProductModule { }
