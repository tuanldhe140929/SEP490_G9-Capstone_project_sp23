import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CommonRoutingModule } from './common-routing.module';
import { ProductCollectionComponent } from './product-collection/product-collection.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input'
import { FormsModule } from '@angular/forms';
import {MatListModule} from '@angular/material/list';
import { HomepageComponent } from './homepage/homepage.component';
import { HeaderComponent } from './homepage/header/header.component';
import { FooterComponent } from './homepage/footer/footer.component';
import { HomeComponent } from './homepage/home/home.component';
import { ShoppingComponent } from './shopping/shopping.component';
import { ShopComponent } from './shopping/shop/shop.component';


@NgModule({
  declarations: [
    ProductCollectionComponent,
    HomepageComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ShoppingComponent,
    ShopComponent,
  ],
  imports: [
    BrowserModule,
    CommonRoutingModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatInputModule,
    FormsModule
  ]
})
export class CommonModule { }
