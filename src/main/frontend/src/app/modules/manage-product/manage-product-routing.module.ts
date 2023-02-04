import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../helpers/auth.guard';
import { NewProductComponent } from './new-product/new-product.component';


const routes: Routes = [{
  path: 'product/new/:productId',
  component: NewProductComponent,
  title: "Create new product",
  canActivate: [AuthGuard]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageProductRoutingModule { }
