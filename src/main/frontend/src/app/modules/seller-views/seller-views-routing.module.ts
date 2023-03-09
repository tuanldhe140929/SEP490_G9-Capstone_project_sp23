import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellerGuardGuard } from '../../guards/seller-guard.guard';
import { UpdateProductComponent } from './update-product/update-product.component';

const routes: Routes = [
  {
    path: 'product/update/:productId/:version',
    component: UpdateProductComponent,
    title: "Update product",
    canActivate: [SellerGuardGuard]
  },
  {
    path: 'product/update/:productId',
    component: UpdateProductComponent,
    title: "Update product",
    canActivate: [SellerGuardGuard]
  },
  {
    path: 'product/new/:productId',
    component: UpdateProductComponent,
    title: "Update product",
    canActivate: [SellerGuardGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerViewsRoutingModule { }
