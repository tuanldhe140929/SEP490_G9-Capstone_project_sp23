import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddInspectorComponent } from '../add-inspector/add-inspector.component';
import { ToastrService } from 'ngx-toastr';
import { UpdateInspectorComponent } from '../update-inspector/update-inspector.component';
import { FormBuilder, Validators } from '@angular/forms';
import { User } from 'src/app/DTOS/User';
import { MatTableDataSource} from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSourcePaginator } from '@angular/material/table';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-inspector-list',
  templateUrl: './inspector-list.component.html',
  styleUrls: ['./inspector-list.component.css'],
})

export class InspectorListComponent implements OnInit{


  constructor(private forAdminService: ForAdminService, private httpClient: HttpClient, private dialog: MatDialog, private toastr: ToastrService, private formBuilder: FormBuilder){}

  inspectorList: User[] = [];
  editInspector: User|undefined;
  username: any;

  ngOnInit(): void {
  }

  addInspectorForm = this.formBuilder.group({
    "email": ['',[Validators.required,Validators.email]],
    "username": ['',[Validators.required,Validators.minLength(4),Validators.maxLength(10), Validators.pattern("^[a-zA-Z0-9_]*$")]],
    "password": ['',[Validators.required,Validators.minLength(8),Validators.maxLength(30)]],
    "repassword": ['',[Validators.required,Validators.minLength(8),Validators.maxLength(30)]]
  })

  

  // public getAllInspectors(): any{
  //   this.forAdminService.getAllInspectors().subscribe(
  //     response => {
  //       this.inspectorList = response;
  //     }
  //   )
  // }

  public onOpenModal(inspector: any, mode: string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if(mode === 'add'){
      button.setAttribute('data-target', '#addInspectorModal');
    }
    if (mode === 'update'){
      this.editInspector = inspector;
      button.setAttribute('data-target', '#updateInspectorModal');
    }
    if(mode === 'delete'){
      this.editInspector = inspector;
      button.setAttribute('data-target', '#deleteInspectorModal');
    }
    container?.appendChild(button);
    button.click();
  }

  updateInspectorForm = this.formBuilder.group({
    "email": ['',[Validators.required,Validators.email]],
    "username": ['',[Validators.required,Validators.minLength(4),Validators.maxLength(10), Validators.pattern("^[a-zA-Z0-9_]*$")]],
  })

  // public onAddInspector() {
  //   if (this.addInspectorForm.valid) {
  //     this.forAdminService.addInspector(this.addInspectorForm.value).subscribe(
  //       data => {
  //         console.log(data);
  //         this.toastr.success('Inspector added successfully!');
  //       },
  //     );
  //   }
  // }

  // public resetAddForm(){
  //   this.addInspectorForm.reset();
  // }

  // public onUpdateInspector(){
  //   if(this.addInspectorForm.valid){
  //     this.forAdminService.updateInspector(this.updateInspectorForm.value).subscribe(
  //       data => {
  //         console.log(data);
  //         this.toastr.success('Inspector updated successfully!')
  //       }
  //     )
  //   }
  // }

  // public onDeleteInspector(id: number| undefined){
  //   this.forAdminService.deleteInspector(id).subscribe(
  //     data => {
  //       console.log(data);
  //       this.toastr.success('Inspector deleted successfully!')
  //     }
  //   )
  // }

  get addform(){
    return this.addInspectorForm.controls;
  }

  get updateform(){
    return this.updateInspectorForm.controls;
  }

  get password(){
    return this.addInspectorForm.controls.password;
  }

  get repassword(){
    return this.addInspectorForm.controls.repassword;
  }

  onPasswordMatch(): boolean{
    if(this.password.value==this.repassword.value){
      return true;
    }else{
      return false;
    }
  }

  onAdd(){
    this.dialog.open(AddInspectorComponent, {width: '70%'});
  }

}


