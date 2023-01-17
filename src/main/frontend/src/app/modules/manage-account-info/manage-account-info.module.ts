import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ManageAccountInfoRoutingModule } from './manage-account-info-routing.module';
import { ProfileComponent } from './profile/profile.component';


@NgModule({
  declarations: [
    ProfileComponent
  ],
  imports: [
    CommonModule,
    ManageAccountInfoRoutingModule
  ]
})
export class ManageAccountInfoModule { }
