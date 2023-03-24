import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './modules/guest-views/home/home.component';
import { AddviolationComponent } from './modules/staff-views/addviolation/addviolation.component';

const routes: Routes = [
{path: 'addviolation', component: AddviolationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
