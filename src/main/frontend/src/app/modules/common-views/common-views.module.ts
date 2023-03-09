import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommonViewsRoutingModule } from './common-views-routing.module';
import { ErrorComponent } from './error/error.component';


@NgModule({
  declarations: [
    ErrorComponent
  ],
  imports: [
    CommonModule,
    CommonViewsRoutingModule
  ]
})
export class CommonViewsModule { }
