import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductCollectionComponent } from './product-collection/product-collection.component';

const routes: Routes = [{
	path:':username',
	component:ProductCollectionComponent
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CommonRoutingModule { }
