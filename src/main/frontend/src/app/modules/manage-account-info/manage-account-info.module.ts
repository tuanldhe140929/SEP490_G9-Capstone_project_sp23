import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ManageAccountInfoRoutingModule } from './manage-account-info-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuardService } from 'src/app/helpers/AuthGuardService';


@NgModule({
  declarations: [
    ProfileComponent
  ],
  imports: [
    CommonModule,
    ManageAccountInfoRoutingModule
  ],
  providers:[AuthGuardService]
})
export class ManageAccountInfoModule { }
