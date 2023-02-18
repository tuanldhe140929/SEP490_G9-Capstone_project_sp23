import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../helpers/auth.guard';
import { SellerGuardGuard } from '../../helpers/seller-guard.guard';
import { NewProductComponent } from './new-product/new-product.component';


const routes: Routes = [{
  path: 'product/update/:productId',
  component: NewProductComponent,
  title: "Update product",
  canActivate: [SellerGuardGuard]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageProductRoutingModule { }
