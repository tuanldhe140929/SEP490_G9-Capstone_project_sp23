import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { CommonViewsRoutingModule } from './common-views-routing.module';
import { ErrorComponent } from './error/error.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';


@NgModule({
  declarations: [
    ErrorComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    CommonViewsRoutingModule,
    MatAutocompleteModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    ErrorComponent,
    HeaderComponent,
    FooterComponent
  ]
})
export class CommonViewsModule { }
