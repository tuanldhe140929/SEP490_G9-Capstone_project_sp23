import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckOutComponent } from './check-out/check-out.component';
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
  path: 'shopping',
  component: ShoppingComponent
},
{
  path: ':username/:productId',
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
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForUsersRoutingModule { }
