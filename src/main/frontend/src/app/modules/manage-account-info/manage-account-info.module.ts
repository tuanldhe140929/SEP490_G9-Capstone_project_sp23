import { NgModule } from '@angular/core';
import { ManageAccountInfoRoutingModule } from './manage-account-info-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from '../../helpers/auth.guard';


@NgModule({
  declarations: [
    ProfileComponent
  ],
  imports: [
    ManageAccountInfoRoutingModule
  ],
  providers: [AuthGuard]
})
export class ManageAccountInfoModule { }
