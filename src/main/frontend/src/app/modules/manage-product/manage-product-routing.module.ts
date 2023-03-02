import { ProductCollectionComponent } from './product-collection/product-collection.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../helpers/auth.guard';
import { SellerGuardGuard } from '../../helpers/seller-guard.guard';
import { NewProductComponent } from './new-product/new-product.component';


const routes: Routes = [{
  path: 'product/update/:productId/:version',
  component: NewProductComponent,
  title: "Update product",
  canActivate: [SellerGuardGuard]
}, {
  path: 'product/new/:productId/:version',
  component: NewProductComponent,
  title: "Update product",
  canActivate: [SellerGuardGuard]
},
{
  path: 'product/update/:productId',
  component: NewProductComponent,
  title: "Update product",
  canActivate: [SellerGuardGuard]
},
{
  path: ':userName',
  component: ProductCollectionComponent,
  title: "User product collection",
}
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageProductRoutingModule { }
