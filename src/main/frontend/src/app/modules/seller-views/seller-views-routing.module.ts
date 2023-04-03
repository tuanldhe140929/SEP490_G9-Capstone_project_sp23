import { PayoutHistoryComponent } from './payout-history/payout-history.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellerGuardGuard } from '../../guards/seller-guard.guard';
import { UpdateProductComponent } from './update-product/update-product.component';

const routes: Routes = [
  {
    path: 'product/update/:productId/:version',
    component: UpdateProductComponent,
    title: "Cập nhật sản phẩm",
    canActivate: [SellerGuardGuard]
  },
  {
    path: 'product/update/:productId',
    component: UpdateProductComponent,
    title: "Cập nhật sản phẩm",
    canActivate: [SellerGuardGuard]
  },
  {
    path: 'product/new/:productId',
    component: UpdateProductComponent,
    title: "Cập nhật sản phẩm",
    canActivate: [SellerGuardGuard]
  },
  {
    path: 'payout',
    component: PayoutHistoryComponent,
    title: "Payout",
    canActivate: [SellerGuardGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerViewsRoutingModule { }
