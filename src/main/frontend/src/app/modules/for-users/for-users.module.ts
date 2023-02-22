import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ForUsersRoutingModule } from './for-users-routing.module';
import { HomepageComponent } from './homepage/homepage.component';
import { ShoppingComponent } from './shopping/shopping.component';
import { HeaderComponent } from './homepage/header/header.component';
import { FooterComponent } from './homepage/footer/footer.component';
import { HomeComponent } from './homepage/home/home.component';
import { ShopComponent } from './shopping/shop/shop.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { SingleComponent } from './product-details/single/single.component';
import { MyCartComponent } from './my-cart/my-cart.component';
import { CartComponent } from './my-cart/cart/cart.component';
import { CheckOutComponent } from './check-out/check-out.component';
import { CheckoutComponent } from './check-out/checkout/checkout.component';
import { NotFoundComponent } from './not-found/not-found.component';


@NgModule({
  declarations: [
    HomepageComponent,
    ShoppingComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ShopComponent,
    ProductDetailsComponent,
    SingleComponent,
    MyCartComponent,
    CartComponent,
    CheckOutComponent,
    CheckoutComponent,
    NotFoundComponent,
  ],
  imports: [
    CommonModule,
    ForUsersRoutingModule
  ]
})
export class ForUsersModule { }
