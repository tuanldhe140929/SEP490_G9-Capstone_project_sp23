import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateReportComponent } from './create-report/create-report.component';
import { ReportListComponent } from './report-list/report-list.component';


const routes: Routes = [
  {
    path: 'reports', component: ReportListComponent
  },
  {
    path: 'create-report', component: CreateReportComponent 
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
