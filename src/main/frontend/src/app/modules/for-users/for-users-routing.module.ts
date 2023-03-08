import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../helpers/auth.guard';
import { CheckOutComponent } from './check-out/check-out.component';
import { DownloadComponent } from './download/download.component';
import { HomepageComponent } from './homepage/homepage.component';
import { MyCartComponent } from './my-cart/my-cart.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { ShoppingComponent } from './shopping/shopping.component';

const routes: Routes = [
{
  path: 'homepage',
  component: HomepageComponent
},
{
  path: 'shopping/:keyword',
  component: ShoppingComponent
},
{
  path: 'products/:productId',
  component: ProductDetailsComponent,
  title:'Chi tiết sản phẩm'
},
{
  path: 'cart',
  component: MyCartComponent
},
{
  path: 'checkout',
  component: CheckOutComponent
},
  {
    path: 'download/:productId',
    component: DownloadComponent,
    title:'Download',
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForUsersRoutingModule { }
