import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Category } from 'src/app/DTOS/Category';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-update-category',
  templateUrl: './update-category.component.html',
  styleUrls: ['./update-category.component.css']
})
export class UpdateCategoryComponent implements OnInit{

  constructor(private formBuilder: FormBuilder, private forAdminService: ForAdminService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: {nameList: string[], id: number, oldName: string}){}

  ngOnInit(): void {
    this.updateCategoryForm.controls['name'].setValue(this.data.oldName)
  }

  updateCategoryForm = this.formBuilder.group({
    "name": ['',[Validators.required]]
  })

  checkNameExists(){
    if(this.name.value?.toLowerCase().trim()!=this.data.oldName.toLowerCase().trim()){
      for(let i=0;i<this.data.nameList.length;i++){
        if(this.name.value?.toLowerCase().trim()==this.data.nameList[i].toLowerCase().trim()){
          return true;
        }
      }
      return false;
    }else{
      return false;
    }
  }

  get form(){
    return this.updateCategoryForm;
  }

  get name(){
    return this.updateCategoryForm.controls.name;
  }

  updateCategory(id: number){
    if(this.updateCategoryForm.valid){
      if(!this.checkNameExists()){
        this.forAdminService.updateCategory(this.updateCategoryForm.value,id).subscribe(
          response => {
            console.log(response);
            this.toastr.success('Cập nhật tên phân loại thành công!')
          }
        )
      }else{
        this.toastr.error('Tên đã được sử dụng')
      }
      
    }
    
  }
}
