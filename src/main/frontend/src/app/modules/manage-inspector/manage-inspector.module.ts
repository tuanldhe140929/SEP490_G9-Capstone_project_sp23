import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';

import { ManageInspectorRoutingModule } from './manage-inspector-routing.module';
import { AllInspectorsComponent } from './all-inspectors/all-inspectors.component';
import { AddInspectorComponent } from './add-inspector/add-inspector.component';


@NgModule({
  declarations: [
    AllInspectorsComponent,
    AddInspectorComponent
  ],
  imports: [
    CommonModule,
    ManageInspectorRoutingModule,
    BrowserModule
  ]
})
export class ManageInspectorModule { }
