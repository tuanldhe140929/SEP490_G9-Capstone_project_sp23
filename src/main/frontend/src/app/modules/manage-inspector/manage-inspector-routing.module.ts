import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddInspectorComponent } from './add-inspector/add-inspector.component';
import { AllInspectorsComponent } from './all-inspectors/all-inspectors.component';

const routes: Routes = [
  {
    path: 'inspectors',
    component: AllInspectorsComponent
  },
  {
    path: 'addInspector',
    component: AddInspectorComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManageInspectorRoutingModule { }
