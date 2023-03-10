import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../guards/auth.guard';
import { CheckOutComponent } from './check-out/check-out.component';
import { DownloadComponent } from './download/download.component';
import { HomepageComponent } from './homepage/homepage.component';
import { MyCartComponent } from './my-cart/my-cart.component';
import { ShoppingComponent } from './shopping/shopping.component';

const routes: Routes = [
	{
		path:'homepage',
		component: HomepageComponent
	},
	
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForUsersRoutingModule { }
